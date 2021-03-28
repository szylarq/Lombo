package lombo.utils;

public class NumberToWordsUtils {

    private static final String[] ones = {"", " jeden", " dwa", " trzy", " cztery", " pięć", " sześć", " siedem", " osiem", " dziewięć", "dziesięć", " jedenaście", " dwanaście", " trzynaście", " czternaście", " piętnaście", " szesnaście", " siedemnaście", " osiemnaście", " dziewiętnaście"};
    private static final String[] tens ={"", " dziesięć", " dwadzieścia", " trzydzieści", " czterdzieści", " pięćdziesiąt", " sześćdziesiąt", " siedemdziesiąt", " osiemdziesiąt", " dziewięćdziesiąt"};
    private static final String[] hundreds = {"", " sto", " dwieście", " trzysta", " czterysta", " pięćset", " sześćset", " siedemset", " osiemset", " dziewięćset"};

    public static String convertNumberToWords(String number){

        String[] wholeAndFraction = number.split(",");

        String whole = wholeAndFraction[0];
        String fraction = wholeAndFraction[1];

        int lastDigit = Integer.parseInt(String.valueOf(whole.charAt(whole.length() - 1)));

        int lastTwoDigits = whole.length() == 1 ? lastDigit : lastDigit
                + Integer.parseInt(String.valueOf(whole.charAt(whole.length() - 2))) * 10;

        String result = reverse(buildWordsResult(whole));
        result += properZlotyEnding(lastTwoDigits, whole.length());

        lastTwoDigits = Integer.parseInt(String.valueOf(fraction.charAt(fraction.length() - 1)))
                + Integer.parseInt(String.valueOf(fraction.charAt(fraction.length() - 2))) * 10;

        result += reverse(buildWordsResult(fraction));
        result += properGroszEnding(lastTwoDigits);

        return result;
    }

    private static String reverse(String stringToReverse){

        String[] resultElements = stringToReverse.split(" ");

        StringBuilder resultStringBuilder = new StringBuilder();

        for (int i = resultElements.length - 1; i >= 0; i--) {

            resultStringBuilder.append(" ").append(resultElements[i]);
        }

        return resultStringBuilder.toString();
    }

    private static String buildWordsResult(String numberToConvert){

        int length = numberToConvert.length();
        String[] digitsTemp = numberToConvert.split("");

        int[] digits = new int[length];
        int[] numbers = new int[length];

        for(int i = 0; i < length; i++){

            digits[i] = Integer.parseInt(digitsTemp[i]);

            numbers[i] = (int) (digits[i] * Math.pow(10, length - i - 1));
        }

        String result = "";

        if(length > 1){

            int temp = numbers[length - 1] + numbers[length - 2];

            if(temp == 0 && length == 2){

                result += " zero";
            }
            else if(temp < 20){

                result += ones[temp];
            }
            else {

                result += ones[digits[length - 1]] + tens[digits[length - 2]];
            }
        }
        else{

            result += digits[length - 1] == 0 ? " zero" : ones[digits[length - 1]];
        }

        if(length > 2) {

            result += hundreds[digits[length - 3]];
        }
        if(length == 4){

            result += properThousandsEnding(digits[length - 4]);
            result += ones[digits[length - 4]];
        }
        if(length > 4){

            int temp = (numbers[length - 4] + numbers[length - 5]) / 1000;

            result += properThousandsEnding(temp);
            result += temp < 20 ? ones[temp] : ones[digits[length - 4]] + tens[digits[length - 5]];
        }

        if(length > 5) {

            result += hundreds[digits[length - 6]];
        }

        return result;
    }

    private static String properThousandsEnding(int number){

        int temp = number < 20 ? number : number % 10;

        if(temp == 1){

            return " tysiąc";
        }
        else if(temp < 5 && temp > 1){

            return " tysiące";
        }
        else{

            return " tysięcy";
        }
    }

    private static String properZlotyEnding(int number, int wholeNumberLength){

        int temp = number < 20 ? number : number % 10;

        if(temp == 1 && number == 1){

            if(wholeNumberLength == 1){

                return "złoty";
            }
            else{

                return "złotych";
            }

        }
        else if(temp < 5 && temp > 1){

            return "złote";
        }
        else{

            return "złotych";
        }
    }

    private static String properGroszEnding(int number) {

        int temp = number < 20 ? number : number % 10;

        if (temp == 1 && number == 1) {

            return "grosz";
        }
        else if (temp < 5 && temp > 1) {

            return "grosze";
        }
        else {

            return "groszy";
        }
    }
}
