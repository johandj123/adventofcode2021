import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.api.*;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day24 {
    Map<String, Integer> vargen = new HashMap<>();
    Map<String, IntegerFormula> variables = new HashMap<>();
    IntegerFormulaManager imgr;
    BooleanFormulaManager bmgr;

    public static void main(String[] args) throws Exception {
        new Day24().start();
    }

    private void start() throws Exception {
        List<Instruction> instructions = Files.readAllLines(new File("input24.txt").toPath())
                .stream()
                .map(Instruction::new)
                .collect(Collectors.toList());
        vargen.put("w", 0);
        vargen.put("x", 0);
        vargen.put("y", 0);
        vargen.put("z", 0);
        try (SolverContext context = SolverContextFactory.createSolverContext(SolverContextFactory.Solvers.PRINCESS)) {
            imgr = context.getFormulaManager().getIntegerFormulaManager();
            bmgr = context.getFormulaManager().getBooleanFormulaManager();
            IntegerFormula[] digits = new IntegerFormula[14];
            int currentDigitIndex = 0;
            List<BooleanFormula> constraints = new ArrayList<>();
            for (int i = 0; i < digits.length; i++) {
                digits[i] = imgr.makeVariable("d" + i);
                constraints.add(imgr.greaterOrEquals(digits[i], imgr.makeNumber(1L)));
                constraints.add(imgr.lessOrEquals(digits[i], imgr.makeNumber(9L)));
            }
            constraints.add(imgr.equal(digits[0], imgr.makeNumber(9)));
            constraints.add(imgr.equal(digits[1], imgr.makeNumber(1)));
            constraints.add(imgr.equal(digits[2], imgr.makeNumber(8)));
            constraints.add(imgr.equal(digits[3], imgr.makeNumber(1)));
            constraints.add(imgr.equal(digits[4], imgr.makeNumber(1)));
            constraints.add(imgr.equal(digits[5], imgr.makeNumber(2)));
            constraints.add(imgr.equal(digits[6], imgr.makeNumber(1)));
            constraints.add(imgr.equal(digits[7], imgr.makeNumber(1)));
            constraints.add(imgr.equal(digits[8], imgr.makeNumber(6)));
            constraints.add(imgr.equal(digits[9], imgr.makeNumber(1)));

            constraints.add(imgr.equal(getVar("w"), imgr.makeNumber(0)));
            constraints.add(imgr.equal(getVar("x"), imgr.makeNumber(0)));
            constraints.add(imgr.equal(getVar("y"), imgr.makeNumber(0)));
            constraints.add(imgr.equal(getVar("z"), imgr.makeNumber(0)));
            for (Instruction instruction : instructions) {
                IntegerFormula result = getNextVar(instruction.var1);
                if (instruction.op == Op.INP) {
                    constraints.add(imgr.equal(result, digits[currentDigitIndex++]));
                } else {
                    IntegerFormula if1 = getVar(instruction.var1);
                    IntegerFormula if2 = getVar2(instruction);
                    switch (instruction.op) {
                        case ADD:
                            constraints.add(imgr.equal(result, imgr.add(if1, if2)));
                            break;
                        case MUL:
                            constraints.add(imgr.equal(result, imgr.multiply(if1, if2)));
                            break;
                        case DIV:
                            constraints.add(imgr.equal(result, imgr.divide(if1, if2)));
                            break;
                        case MOD:
                            constraints.add(imgr.equal(result, imgr.modulo(if1, if2)));
                            break;
                        case EQL:
                            BooleanFormula bf = imgr.equal(if1, if2);
                            BooleanFormula bfnot = bmgr.not(imgr.equal(if1, if2));
                            BooleanFormula r0 = imgr.equal(result, imgr.makeNumber(0));
                            BooleanFormula r1 = imgr.equal(result, imgr.makeNumber(1));
                            constraints.add(bmgr.equivalence(bf, r1));
                            constraints.add(bmgr.equivalence(bfnot, r0));
                            break;
                    }
                }
                vargen.put(instruction.var1, vargen.get(instruction.var1) + 1);
            }
            constraints.add(imgr.equal(getVar("z"), imgr.makeNumber(0)));

            try (ProverEnvironment prover = context.newProverEnvironment(SolverContext.ProverOptions.GENERATE_MODELS)) {
                for (BooleanFormula constraint : constraints) {
                    prover.addConstraint(constraint);
                }
                if (prover.isUnsat()) {
                    System.out.println("Unsat");
                }
                try (Model model = prover.getModel()) {
                    for (IntegerFormula integerFormula : digits) {
                        System.out.print(model.evaluate(integerFormula));
                    }
                    System.out.println();
                }
            }
        }
    }

    private void disallowSolution(List<BooleanFormula> constraints, IntegerFormula[] digits, String s) {
        BooleanFormula b = null;
        for (int i = 0; i < s.length(); i++) {
            BooleanFormula nb = imgr.equal(digits[i], imgr.makeNumber(s.charAt(i) - '0'));
            if (b == null) {
                b = nb;
            } else {
                b = bmgr.and(b, nb);
            }
        }
        constraints.add(bmgr.not(b));
    }

    public IntegerFormula getVar(String var) {
        String name = var + vargen.get(var);
        return variables.computeIfAbsent(name, key -> imgr.makeVariable(key));
    }

    public IntegerFormula getNextVar(String var) {
        String name = var + (vargen.get(var) + 1);
        return variables.computeIfAbsent(name, key -> imgr.makeVariable(key));
    }

    public IntegerFormula getVar2(Instruction instruction) {
        if (instruction.var2 == null) {
            return imgr.makeNumber(instruction.value2);
        } else {
            return getVar(instruction.var2);
        }
    }

    enum Op {
        INP,
        ADD,
        MUL,
        DIV,
        MOD,
        EQL
    }

    static class Instruction {
        Op op;
        String var1;
        String var2;
        int value2;

        public Instruction(String line) {
            String[] split = line.split(" ");
            op = Op.valueOf(split[0].toUpperCase());
            var1 = split[1];
            if (split.length >= 3) {
                try {
                    value2 = Integer.parseInt(split[2]);
                } catch (NumberFormatException e) {
                    var2 = split[2];
                }
            }
        }

        @Override
        public String toString() {
            return "Instruction{" +
                    "op=" + op +
                    ", var1='" + var1 + '\'' +
                    ", var2='" + var2 + '\'' +
                    ", value2=" + value2 +
                    '}';
        }
    }
}
