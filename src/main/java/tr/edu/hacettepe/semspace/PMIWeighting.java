package tr.edu.hacettepe.semspace;

import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;

public class PMIWeighting {
	
	public static void applyWeighting(Matrix matrix) {
		double[] rowSums = new double[matrix.rowSize()];
		double sums = 0;
		
		for (int i = 0; i < matrix.rowSize(); i++) {
			Vector vector = matrix.viewRow(i);
			rowSums[i] = vector.zSum();
			sums += rowSums[i];
		}
		
		for (int i = 0; i < matrix.rowSize(); i++) {
			Vector vector = matrix.viewRow(i);
			Iterable<Element> elements = vector.nonZeroes();
			for (Element element : elements) {
				int j = element.index();
				
				double pmi = (sums * element.get()) / (rowSums[i]*rowSums[j]);
				element.set(pmi);
			}
		}
				
	}

}
