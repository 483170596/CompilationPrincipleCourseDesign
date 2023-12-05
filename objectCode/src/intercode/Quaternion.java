package intercode;

//四元式
public class Quaternion {
    public String op;
    public String arg1;
    public String arg2;
    public String result;

    public String toTAC() {
        if (op.equals("=")) {
            return result + "=" + arg1;
        } else if (op.equals("<")) {
            return result + "<" + arg1;
        } else if (op.equals(">")) {
            return result + ">" + arg1;
        } else if (op.equals("<=")) {
            return result + "<=" + arg1;
        } else if (op.equals(">=")) {
            return result + ">=" + arg1;
        } else if (op.equals("==")) {
            return result + "==" + arg1;
        } else if (op.equals("!=")) {
            return result + "!=" + arg1;
        } else if (op.equals("&&")) {
            return result + "&&" + arg1;
        } else if (op.equals("||")) {
            return result + "||" + arg1;
        } else if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("%")) {
            return result + "=" + arg1 + op + arg2;
        } else if (op.equals("j")) {
            return "goto " + result;
        } else if (op.equals("jq")) {
            return "if " + arg1 + " goto " + result;
        } else if (op.equals("param")) {
            return "param " + result;
        } else if (op.equals("call")) {
            return "call " + arg1 + "," + result;
        } else if (op.equals("halt")) {
            return "halt";
        }
        throw new IllegalArgumentException("Unsupported operation: " + op);
    }

    public Quaternion() {
    }

    public Quaternion(String op, String arg1, String arg2, String result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    /**
     * 获取
     *
     * @return op
     */
    public String getOp() {
        return op;
    }

    /**
     * 设置
     *
     * @param op
     */
    public void setOp(String op) {
        this.op = op;
    }

    /**
     * 获取
     *
     * @return arg1
     */
    public String getArg1() {
        return arg1;
    }

    /**
     * 设置
     *
     * @param arg1
     */
    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    /**
     * 获取
     *
     * @return arg2
     */
    public String getArg2() {
        return arg2;
    }

    /**
     * 设置
     *
     * @param arg2
     */
    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    /**
     * 获取
     *
     * @return result
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置
     *
     * @param result
     */
    public void setResult(String result) {
        this.result = result;
    }

    public String toString() {
        return op + " " + result + " " + arg1 + " " + arg2 + "\n";
    }
}
