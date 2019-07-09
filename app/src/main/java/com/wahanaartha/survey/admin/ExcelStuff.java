package com.wahanaartha.survey.admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;


import com.wahanaartha.survey.model.ReportQuestion;
import com.wahanaartha.survey.model.ReportResponden;
import com.wahanaartha.survey.model.ReportSelectedAnswer;
import com.wahanaartha.survey.model.ReportSurvey;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import static com.wahanaartha.survey.admin.AdminAddSurveyQuestionActivity.QuestionType.CheckList;

/**
 * Created by lely
 */

public class ExcelStuff {

    private Context context;
    private String month;
    private String year;
    public ExcelStuff(Context context) {
        this.context = context;
    }

    public void createReport(List<ReportSurvey> surveys, String month, String year) {

        String path = "Report" + month+"-"+year+ ".xls";
        this.month = month;
        this.year = year;
//         Create new workbook
        Workbook workbook;
        workbook = new HSSFWorkbook();


        for (ReportSurvey survey : surveys) {
//            Log.i("INFO", survey.getQuestions().toString());
            createSheetWithSurvey(survey, workbook);
        }


        boolean success = false;
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + path;
        File dir = new File(fullPath);
        try {
            OutputStream fOut = new FileOutputStream(dir);
            workbook.write(fOut);
            success = true;
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            Log.e("saveToExternalStorage()", e.getMessage());
        }
        if (success) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", dir);
                intent.setDataAndType(fileUri,"application/vnd.ms-excel");
            } else {
                intent.setDataAndType(Uri.fromFile(dir),"application/vnd.ms-excel");
            }

            try {
                context.startActivity(intent);
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, "You need to have Microsoft Office Excel Installed here", Toast.LENGTH_LONG).show();
            } finally {
                Toast.makeText(context, "Success create report on " + dir.getPath() , Toast.LENGTH_LONG).show();
            }
        }

    }

    private final java.util.Random rand = new java.util.Random();

    // consider using a Map<String,Boolean> to say whether the identifier is being used or not
    private final Set<String> identifiers = new HashSet<String>();

    private String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
            for(int i = 0; i < length; i++)
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            if (identifiers.contains(builder.toString()))
                builder = new StringBuilder();
        }
        return builder.toString();
    }

    private void createSheetWithSurvey(ReportSurvey selectedSurvey, Workbook workbook) {
        List<ReportResponden> respondens = selectedSurvey.getRespondens();

        Cell cell;
        Sheet sheet = workbook.createSheet(selectedSurvey.getTitle());

        int curColumn = 0;
        int curRow = 0;

        CellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);

        Font surveyTitleFont = workbook.createFont();
        surveyTitleFont.setBold(true);
        surveyTitleFont.setFontHeight((short)300);

        CellStyle surveyTitleStyle = workbook.createCellStyle();
        surveyTitleStyle.setFont(surveyTitleFont);

        // create survey title
        Row row = sheet.createRow(curRow);
        cell = row.createCell(curColumn);
        cell.setCellStyle(surveyTitleStyle);
        cell.setCellValue("REPORT SURVEY");


        curRow++;
        curColumn = 0;
        row = sheet.createRow(curRow);
        cell = row.createCell(curColumn);
        cell.setCellValue(selectedSurvey.getTitle().toUpperCase());


        curRow++;
        curColumn = 0;
        row = sheet.createRow(curRow);
        cell = row.createCell(curColumn);
        if(month.equals("01")){
            month = "Januari";
        }else if(month.equals("02")){
            month = "Februari";
        }else if(month.equals("03")){
            month = "Maret";
        }else if(month.equals("04")){
            month = "April";
        }else if(month.equals("05")){
            month = "Mei";
        }else if(month.equals("06")){
            month = "Juni";
        }else if(month.equals("07")){
            month = "Juli";
        }else if(month.equals("08")){
            month = "Agustus";
        }else if(month.equals("09")){
            month = "September";
        }else if(month.equals("10")){
            month = "Oktober";
        }else if(month.equals("11")){
            month = "November";
        }else if(month.equals("12")){
            month = "Desember";
        }

        cell.setCellValue("Periode : "+month+" - "+year);
        curRow += 3;
        curColumn = 0;

        row = sheet.createRow(curRow);
        ArrayList<String> header = getHeader();
        // create header title
        for (int i = 0; i < header.size(); i++, curColumn++) {
            String headerTitle = header.get(i);
            cell = row.createCell(curColumn);
            sheet.setColumnWidth(curColumn, headerTitle.length() * 300);
            cell.setCellValue(headerTitle);
        }

        ReportQuestion question;
       //  list all the question on header
        for (int i = 0; i < selectedSurvey.getQuestions().size(); i++, curColumn++) {
            question = selectedSurvey.getQuestions().get(i);
            cell = row.createCell(curColumn);
            sheet.setColumnWidth(curColumn, question.getTitle().length() * 300);
            cell.setCellValue(question.getTitle());
        }


        // iterate through all responden
        for (int i = 0; i < respondens.size(); i++) {
            ReportResponden responden = respondens.get(i);

            curRow++;

            row = sheet.createRow(curRow);

            // create responden info
            curColumn = 0;
            ArrayList<String> respondenInfos = getRespondenInfo(responden, i+1);
            for (int n = 0; n <respondenInfos.size(); n++, curColumn++) {
                String respondenInfo = respondenInfos.get(n);

                cell = row.createCell(curColumn);
                cell.setCellValue(respondenInfo);
            }

            // list all answer
            for (ReportSelectedAnswer answer : responden.getSelectedAnswers()) {
                cell = row.createCell(curColumn);
                cell.setCellValue(answer.getTitle());
                if (answer.getType().equals(CheckList.toString())) {
                    String answerTitle = "";
                    for (String s : answer.getTitle().split("%")) {
                        answerTitle += "\n" + s;
                    }
                    cell.setCellValue(answerTitle);
                    cell.setCellStyle(wrapStyle);
                }
                curColumn++;
            }
        }
    }

    ArrayList<String> getRespondenInfo(ReportResponden responden, int index) {
        ArrayList<String> respondenInfo = new ArrayList<>();

        respondenInfo.add("" + index);
        respondenInfo.add(responden.getName());
        respondenInfo.add(responden.getDealer_name());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(responden.getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        System.out.println(formattedDate);
        respondenInfo.add(formattedDate);
        return respondenInfo;
    }
    ArrayList<String> getHeader() {
        ArrayList<String> header = new ArrayList<>();

        header.add("No.");
        header.add("Nama Responden");
        header.add("Nama Dealer");
        header.add("Tanggal");
        return header;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
