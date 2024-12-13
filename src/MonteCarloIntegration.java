import java.util.Scanner;
import java.util.Random;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MonteCarloIntegration {
    public static double MCResult(String funcExpression, String regionExpression, double[][] bounds, int N) {
        int dimensions = bounds.length;
        double volume = 1.0;

        for (double[] bound : bounds) {
            volume *= (bound[1] - bound[0]);
        }

        String[] regions = splitRegions(regionExpression);
        int ri=0;
        Expression funcExpr = new ExpressionBuilder(funcExpression).variables(generateVariables(dimensions)).build();
        Expression[] regionsEx = new Expression[regions.length];
        for (String region: regions){
            Expression regionEx = new ExpressionBuilder(region).variables(generateVariables(dimensions)).operator(Operators.gt).operator(Operators.mt).operator(Operators.mteq).operator(Operators.gteq).build();
            regionsEx[ri]=regionEx;
            ri++;
        }

        double sum = countIntegral(funcExpr, regionsEx, N, bounds, ri);

        return volume * sum / N;
    }

    private static boolean checkRegions(Expression[] regionsEx, int ri){
        boolean k = true;
        for (int i=0; i<ri; i++){
            if(regionsEx[i].evaluate() <= 0){
                k = false;
            }
        }
        return k;
    }

    private static String[] generateVariables(int dimensions) {
        String[] vars = new String[dimensions];
        for (int i = 0; i < dimensions; i++) {
            vars[i] = "x" + i;
        }
        return vars;
    }

    private static String[] splitRegions(String regionExpression) {
        String nd = "&";
        int count = regionExpression.length() - regionExpression.replace(nd, "").length();

        String[] regions = new String[count+1];

        if (count!=0){
            regions = regionExpression.split(nd);
        }
        else{
            regions[0] = regionExpression;
        }
        return regions;
    }

    private static double countIntegral(Expression funcExpr, Expression[] regionsEx, int N, double[][] bounds, int ri){
        double sum = 0.0;
        int insideCount = 0;
        Random rand = new Random();
        int dimensions = bounds.length;

        for (int i = 0; i < N; i++) {
            double[] randomPoint = new double[dimensions];
            for (int d = 0; d < dimensions; d++) {
                randomPoint[d] = bounds[d][0] + (bounds[d][1] - bounds[d][0]) * rand.nextDouble();
                funcExpr.setVariable("x" + d, randomPoint[d]);
                for (int j=0; j<ri;j++){
                    regionsEx[j].setVariable("x" + d, randomPoint[d]);
                }
            }
            if (checkRegions(regionsEx, ri)) {
                sum += funcExpr.evaluate();
                insideCount++;
            }
        }

        if (insideCount == 0) {
            throw new IllegalArgumentException("Все точки вне области. Проверьте границы или область интегрирования.");
        }
        return sum;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите функцию для интегрирования (например, x0*x0 + x1*x1):");
        String funcExpression = scanner.nextLine();
        System.out.println("Введите область интегрирования (например, x0*x0 + x1*x1 <= 1):");
        String regionExpression = scanner.nextLine();
        System.out.println("Введите количество переменных:");
        int dimensions = scanner.nextInt();

        double[][] bounds = new double[dimensions][2];
        for (int i = 0; i < dimensions; i++) {
            System.out.println("Введите нижнюю границу для переменной x" + i + ":");
            bounds[i][0] = scanner.nextDouble();
            System.out.println("Введите верхнюю границу для переменной x" + i + ":");
            bounds[i][1] = scanner.nextDouble();
        }

        System.out.println("Введите количество случайных точек:");
        int N = scanner.nextInt();

        try {
            double result = MCResult(funcExpression, regionExpression, bounds, N);
            System.out.println("Результат интегрирования: " + result);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        scanner.close();
    }
}
