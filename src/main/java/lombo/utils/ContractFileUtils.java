package lombo.utils;

import lombo.model.Contract;
import lombo.model.ContractLength;
import lombo.model.Customer;
import lombo.model.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import lombo.DAO.DAO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ContractFileUtils {

    public static final String BLANK_CONTRACT_FILE_PATH
            = System.getProperty("user.dir") + "\\src\\main\\resources\\blank_documents\\umowa_lombardowa.xlsx";

    public static final String BLANK_REPAYMENT_CONFIRMATION_FILE_PATH
            = System.getProperty("user.dir") + "\\src\\main\\resources\\blank_documents\\zwrot-pozyczki.xlsx";

    private final DAO dao = new DAO();

    public void writeAndOpenFile(String filePath, Contract contract) throws IOException {

        File outputFile = null;

        if(filePath.equals(BLANK_CONTRACT_FILE_PATH)){

            outputFile = writeFile(mapContractCellsData(contract), filePath);
        }
        else if(filePath.equals(BLANK_REPAYMENT_CONFIRMATION_FILE_PATH)){

            outputFile = writeFile(mapRepaymentConfirmationCellsData(contract), filePath);
        }

        openFileInExcel(outputFile);

        if(outputFile.exists()){

            outputFile.deleteOnExit();
        }
    }

    private void openFileInExcel(File file) throws IOException {

        Desktop.getDesktop().open(file);
    }

    private Map<String, String> mapContractCellsData(Contract contract) {

        Customer customer = contract.getCustomerByCustomerId();
        Product product = contract.getProductByProductId();
        LocalDate dateFrom = contract.getDateFrom();
        ContractLength length = contract.getContractLengthByLengthId();
        Double contractValue = contract.getValue();
        boolean isSellContract = contract.getContractTypeByTypeId().equals(dao.findContractTypeByName("Sprzedaż"));

        Map<String, String> cellValueMap = new HashMap<>();

        cellValueMap.put("A1", isSellContract ? "UMOWA SPRZEDAŻY" : "UMOWA LOMBARDOWA");
        cellValueMap.put("C3", DateUtils.toPolishDateFormat(dateFrom));
        cellValueMap.put("G3", customer.getName() + " " + customer.getSurname());
        cellValueMap.put("C4", customer.getPersonalIdNo());
        cellValueMap.put("F4", customer.getIssuedBy());
        cellValueMap.put("B5", customer.getFatherName());
        cellValueMap.put("E5", customer.getAddress());

        String contractValueFormatted = FilterUtils.formatDouble(contractValue);

        cellValueMap.put("G10", contractValueFormatted);
        cellValueMap.put("B11", NumberToWordsUtils.convertNumberToWords(contractValueFormatted));
        cellValueMap.put("C14", product.getName());

        String productValueFormatted = FilterUtils.formatDouble(product.getValue());

        cellValueMap.put("G18", productValueFormatted);
        cellValueMap.put("B19", NumberToWordsUtils.convertNumberToWords(productValueFormatted));
        cellValueMap.put("C22", isSellContract ? "Sprzedaż" : DateUtils.toPolishDateFormat(dateFrom));
        cellValueMap.put("C23", isSellContract ? "" : DateUtils.toPolishDateFormat(contract.getDateTo()));
        cellValueMap.put("F26", isSellContract
                ? "Sprzedaż" : FilterUtils.formatDouble(length.getStorageRate()) + "% przechowanie + "
                + FilterUtils.formatDouble(length.getLoanRate()) + "% oproc. pożyczki");

        String contractValuePlusInterestFormatted = length == null ? ""
                : getContractValuePlusInterestFormatted(calculateContractValuePlusInterest(contract));

        cellValueMap.put("D27", isSellContract ? "" : "" + contractValuePlusInterestFormatted);
        cellValueMap.put("B28", length == null ? ""
                : NumberToWordsUtils.convertNumberToWords(contractValuePlusInterestFormatted));

        return cellValueMap;
    }

    private Map<String, String> mapRepaymentConfirmationCellsData(Contract contract) {

        ContractLength length = contract.getContractLengthByLengthId();
        Double value = contract.getValue();

        Map<String, String> cellValueMap = new HashMap<>();

        cellValueMap.put("E5", DateUtils.toPolishDateFormat(LocalDate.now()));
        cellValueMap.put("E6", FilterUtils.formatDouble(value));
        cellValueMap.put("E7", FilterUtils.formatDouble(length.getStorageRate()) + "% przech. + "
                + FilterUtils.formatDouble(length.getLoanRate()) + "% oproc. pożyczki");
        cellValueMap.put("E8", FilterUtils.formatDouble(FilterUtils.calculateInterest(value, length)));

        String contractValuePlusInterestFormatted =
                getContractValuePlusInterestFormatted(calculateContractValuePlusInterest(contract));

        cellValueMap.put("E9", contractValuePlusInterestFormatted);
        cellValueMap.put("C10", NumberToWordsUtils.convertNumberToWords(contractValuePlusInterestFormatted));

        return cellValueMap;
    }

    private File writeFile(Map<String, String> cellValueMap, String inputFilePath) throws IOException {

        File contractFile = new File(inputFilePath);

        FileInputStream fileInputStream = new FileInputStream(contractFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        CellAddress cellAddress;
        Cell cell;

        for (String key : cellValueMap.keySet() ){

            cellAddress = new CellAddress(key);
            Row row = sheet.getRow(cellAddress.getRow());
            cell = row.getCell(cellAddress.getColumn());

            cell.setCellValue(cellValueMap.get(key));
        }

        fileInputStream.close();

        File outputFile = File.createTempFile("temp",".xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        return outputFile;
    }

    public static String generateContractNo(){

        LocalDate today = LocalDate.now();

        int monthNumber = today.getMonthValue();
        int dayOfMonthNumber = today.getDayOfMonth();

        Random random = new Random();

        return random.nextInt(900000) + 100000 + "/"
                + (dayOfMonthNumber < 10 ? "0" + dayOfMonthNumber : "" + dayOfMonthNumber)
                + "/" + (monthNumber < 10 ? "0" + monthNumber : "" + monthNumber)  + "/" +
                + today.getYear();
    }

    private Double calculateContractValuePlusInterest (Contract contract){

        ContractLength length = contract.getContractLengthByLengthId();
        Double contractValue = contract.getValue();

        return length == null ? null : contractValue + FilterUtils.calculateInterest(contractValue, length);
    }

    private String getContractValuePlusInterestFormatted (Double valuePlusInterest){

        return FilterUtils.formatDouble(valuePlusInterest);
    }
}
