import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<double[]> features = read_file("src/5");
        double capacity = features.get(0)[0];
        features.remove(0);

        double[] weights = divide_features_into_weights(features);
        double[] values = divide_features_into_values(features);

        List<Integer> characteristicVector = new ArrayList<>();
        double weightsSum = 0;
        double valuesSum = 0;

        int num = (int) Math.pow(2, features.size());
        for (int i = 0; i < num; i++) {
            List<Integer> newCharacteristicVector = calculate_binary(i, features.size());
            double newWeightsSum = calculate_sum(weights, newCharacteristicVector);
            boolean feasibility = check_feasibility(newWeightsSum, capacity);
            if (feasibility) {
                double newValuesSum = calculate_sum(values, newCharacteristicVector);
                if (newValuesSum > valuesSum) {
                    weightsSum = newWeightsSum;
                    valuesSum = newValuesSum;
                    characteristicVector = newCharacteristicVector;
                }
            }
        }
        System.out.println(characteristicVector);
        System.out.println(weightsSum);
        System.out.println(valuesSum);
    }

    public static List<double[]> read_file(String file) {
        List<double[]> inputFeatures = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(" ");
                double[] feature = new double[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    feature[i] = Double.parseDouble(parts[i]);
                }
                inputFeatures.add(feature);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputFeatures;
    }

    public static List<Integer> calculate_binary(int number, int length) {
        List<Integer> binaryDigits = new ArrayList<>();

        while (number > 0) {
            binaryDigits.add(number % 2);
            number = number / 2;
        }

        while (binaryDigits.size() < length) {
            binaryDigits.add(0);
        }

        Collections.reverse(binaryDigits);
        return binaryDigits;
    }

    public static double[] divide_features_into_weights(List<double[]> inputFeatures) {
        double[] featureWeights = new double[inputFeatures.size()];
        for (int i = 0; i < inputFeatures.size(); i++) {
            featureWeights[i] = inputFeatures.get(i)[0];
        }
        return featureWeights;
    }

    public static double[] divide_features_into_values(List<double[]> inputFeatures) {
        double[] featureValues = new double[inputFeatures.size()];
        for (int i = 0; i < inputFeatures.size(); i++) {
            featureValues[i] = inputFeatures.get(i)[1];
        }
        return featureValues;
    }

    public static boolean check_feasibility(double weightSum, double maxCapacity) {
        return weightSum <= maxCapacity;
    }

    public static double calculate_sum(double[] dividedFeatures, List<Integer> binaryDigits) {
        double sum = 0;
        for (int i = 0; i < binaryDigits.size(); i++) {
            if (binaryDigits.get(i) == 1) {
                sum += dividedFeatures[i];
            }
        }
        return sum;
    }
}
