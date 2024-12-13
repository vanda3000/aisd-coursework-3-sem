import net.objecthunter.exp4j.operator.Operator;

public class Operators {

    static Operator gteq = createGreaterThanOrEqualOperator();
    static Operator mteq = createMinorThanOrEqualOperator();
    static Operator gt = createGreaterThanOperator();
    static Operator mt = createMinorThanOperator();

    private static Operator createGreaterThanOrEqualOperator() {
        return new Operator(">=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            @Override
            public double apply(double[] values) {
                return values[0] >= values[1] ? 1d : 0d;
            }
        };
    }

    private static Operator createGreaterThanOperator() {
        return new Operator(">", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            @Override
            public double apply(double[] values) {
                return values[0] > values[1] ? 1d : 0d;
            }
        };
    }

    private static Operator createMinorThanOperator() {
        return new Operator("<", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            @Override
            public double apply(double[] values) {
                return values[0] < values[1] ? 1d : 0d;
            }
        };
    }

    private static Operator createMinorThanOrEqualOperator() {
        return new Operator("<=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            @Override
            public double apply(double[] values) {
                return values[0] <= values[1] ? 1d : 0d;
            }
        };
    }
}
