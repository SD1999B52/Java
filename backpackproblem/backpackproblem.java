import java.util.ArrayList;

class backpackproblem extends transposition {
	public static void main( String[] args ) {
		/*
		double maxWeight = 10;//максимальный вес груза Васи
		int numFood = 4;//количество единиц еды
		int[] energyValue = { 3, 6, 2, 1 };
		double[] weightUnit = { 5, 7, 4, 1 };
		*/
		
		double maxWeight = 23.70;
		int numFood = 6;
		int[] energyValue = { 10, 5, 23, 13, 8, 20 };
		double[] weightUnit = { 15.80, 3.70, 7.70, 10.10, 4.30, 17.70 };
		
		/*
		double maxWeight = 10;
		int numFood = 4;
		int[] energyValue = { 3, 4, 3, 2 };
		double[] weightUnit = { 4, 3, 2, 2 };
		*/
		char[] symbols = new char[energyValue.length];
		for ( int i = 0; i < symbols.length; i++ ) {
			symbols[i] = Integer.toString( i ).charAt( 0 );
		}
		
		char[][] options = getOptionsNoRepeat( symbols );
		int maxEnergy = 0;
		for ( int i = 0; i < options.length; i++ ) {
			int energy = 0;
			double weight = 0;
			for ( int i2 = 0; i2 < options[0].length; i2++ ) {
				double weightValue = weightUnit[Character.getNumericValue( options[i][i2] )];
				if ( weight + weightValue <= maxWeight ) {
					weight += weightValue;
					energy += energyValue[Character.getNumericValue( options[i][i2] )];
				}
			}
			if ( maxEnergy < energy ) {
				maxEnergy = energy;
			}
		}
		System.out.println( maxEnergy );
	}
}