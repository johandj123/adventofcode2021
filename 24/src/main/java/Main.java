import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.api.*;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;

public class Main {
    public static void main(String[] args) throws InvalidConfigurationException, InterruptedException, SolverException {
        try (SolverContext context = SolverContextFactory.createSolverContext(SolverContextFactory.Solvers.SMTINTERPOL)) {
            IntegerFormulaManager imgr = context.getFormulaManager().getIntegerFormulaManager();

            IntegerFormula a = imgr.makeVariable("a");
            IntegerFormula b = imgr.makeVariable("b");
            BooleanFormula f = imgr.equal(a, imgr.multiply(b, imgr.makeNumber(2)));
            BooleanFormula f2 = imgr.equal(a, imgr.makeNumber(100));

            try (ProverEnvironment prover = context.newProverEnvironment(SolverContext.ProverOptions.GENERATE_MODELS)) {
                prover.addConstraint(f);
                prover.addConstraint(f2);
                if  (prover.isUnsat()) {
                    System.out.println("Unsat");
                }
                try (Model model = prover.getModel()) {
                    System.out.println("a = " + model.evaluate(a));
                    System.out.println("b = " + model.evaluate(b));
                }
            }
        }
    }
}
