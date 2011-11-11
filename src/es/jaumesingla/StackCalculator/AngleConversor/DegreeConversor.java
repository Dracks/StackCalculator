package es.jaumesingla.StackCalculator.AngleConversor;



public class DegreeConversor implements ConversorInterface {

	@Override
	public double toProcess(double d) {
		return Math.PI*d/180;
	}

	@Override
	public double toShow(double d) {
		return 180*d/Math.PI;
	}

	@Override
	public int getID() {
		return 1;
	}

}
