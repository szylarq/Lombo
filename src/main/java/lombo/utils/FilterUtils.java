package lombo.utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lombo.DAO.DAO;
import lombo.model.ContractLength;
import lombo.model.ContractType;
import lombo.model.Customer;
import lombo.model.Product;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FilterUtils {

    public static final double MINIMAL_INTEREST_VALUE = 5.0;

    public static Double calculateInterest(Double basicValue, ContractLength length){

        double interestValue = basicValue * (length.getLoanRate() + length.getStorageRate()) / 100;

        return interestValue < MINIMAL_INTEREST_VALUE ? MINIMAL_INTEREST_VALUE :
                basicValue * (length.getLoanRate() + length.getStorageRate()) / 100;
    }

    public static boolean isStringValueDouble(String value){

        return value.length() != 0 && Pattern.compile("\\d+(([.,])\\d+)?").matcher(value).matches();
    }

    public static boolean isValueWithinRange(String valueFrom, String valueTo, double valueToCheck){

        if(isStringValueDouble(valueFrom) && !isStringValueDouble(valueTo)){

            return valueToCheck >= Double.parseDouble(valueFrom);
        }
        else if(!isStringValueDouble(valueFrom) && isStringValueDouble(valueTo)){

            return valueToCheck <= Double.parseDouble(valueTo);
        }
        else {

            return valueToCheck >= Double.parseDouble(valueFrom) && valueToCheck <= Double.parseDouble(valueTo);
        }
    }

    public static<T> Stream<T> skipOrFilter(Stream<T> stream, Boolean conditionToSkip, Predicate<T> filteringPredicate){

        return conditionToSkip ? stream : stream.filter(filteringPredicate);
    }

    public static <T> Stream<T> filterStreamByPredicates(Stream<T> stream, Map<Predicate<T>, Boolean> conditionsPredicates){

        Set<Predicate<T>> keys = conditionsPredicates.keySet();

        for(Predicate<T> key : keys){

            stream = skipOrFilter(stream, conditionsPredicates.get(key), key);
        }

        return stream;
    }

    public static boolean checkIfCorrectDataProvided(ComboBox<Product> productComboBox, ComboBox<Customer> customerComboBox,
                                                     ComboBox<ContractType> typeComboBox,
                                                     ComboBox<ContractLength> lengthComboBox,
                                                     DatePicker dateFromPicker, TextField valueTextField,
                                                     DAO dao){

        ContractLength length = lengthComboBox.getValue();
        ContractType type = typeComboBox.getValue();

        return productComboBox.getValue() != null && customerComboBox.getValue() != null
                && typeComboBox.getValue() != null && dateFromPicker.getValue() != null
                && FilterUtils.isStringValueDouble(valueTextField.getText())
                && (length != null || type.equals(dao.findContractTypeByName("Sprzedaż")));
    }

    public static String formatDouble(Double number){

        return String.format(Locale.getDefault(), "%.2f", number);
    }

    public static String changeCommaToDot(String numberWithComma){

        return numberWithComma.replaceAll(",",".");
    }
}
