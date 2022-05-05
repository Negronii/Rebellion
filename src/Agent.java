public class Agent extends Turtle{

    public final int id;
    public final double riskAversion = Params.random.nextDouble();
    public final double perceivedHardship = Params.random.nextDouble();
    public boolean isActive;
    public int jailTerm;

    public Agent(int id, int x, int y) {
        super(x, y);
        this.id = id;
    }

    public void determineBehavior(){

    }

    public void goToJail(int terms){
        jailTerm = terms;
    }
}
