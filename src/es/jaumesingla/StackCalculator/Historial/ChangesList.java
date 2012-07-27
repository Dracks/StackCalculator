package es.jaumesingla.StackCalculator.Historial;

import es.jaumesingla.StackCalculator.DataModel;

public class ChangesList {
	public interface changedData{
		public void undo(DataModel d);
	}
}
