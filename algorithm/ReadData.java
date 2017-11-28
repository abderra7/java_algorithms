package algorithm;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadData {
    public static void main(String[] args) 
    {
        try {

            FileInputStream file = new FileInputStream(new File("/tmp/e.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            int grupId = Integer.valueOf(sheet.getRow(0).getCell(0).toString().replaceAll("\\.0", ""));

            while(rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                //For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                String name ="";
                int partido=0,tirs_ficats=0,tirs_intentats=0,lliures_ficats=0,lliures_intentats=0,triples_ficats=0,triples_intentats=0;
                float rebot_def=0, rebot_of=0,asis=0,rec=0,perd=0,taps_favor=0,taps_contra=0,faltes_come=0,faltes_rebudes=0;
                while(cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    switch(cell.getCellType()) 
                    {
                    
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.println("boolean===>>>"+cell.getBooleanCellValue() + cell.getCellType() + "\t");
//write hibernate lines here to store it in your domain
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
//write hibernate lines here to store it in your domain
                            if(cell.getColumnIndex() == 	1) {
                                partido = (int) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 15) {
                                asis = partido * (float) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 3) {
                            	   tirs_ficats = (int) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 4) {
                            	   tirs_intentats = (int) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 16) {
                                rec = partido * (float) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 6) {
                            	triples_ficats = (int) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 7) {
                            	triples_intentats = (int) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 17) {
                                perd = partido * (float) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 9) {
                            	lliures_ficats = (int) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 10) {
                            	lliures_intentats = (int) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 13) {
                                rebot_def = partido * (float) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 14) {
                                rebot_of = partido * (float) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 18) {
                            	taps_favor = partido * (float)cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 19) {
                            	taps_contra = partido * (float) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 20) {
                            	faltes_come = partido * (float) cell.getNumericCellValue();
                            }else if(cell.getColumnIndex() == 21) {
                            	faltes_rebudes = partido * (float) cell.getNumericCellValue();
                            }
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.println("Equip===>>>"+cell.getStringCellValue() + "\t");
                            
                            name = cell.getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_FORMULA: 
                            // Re-run based on the formula type
                    	    System.out.println(cell.getCachedFormulaResultType());
                    	    		break;
                           
                    }
//                    		System.out.println(cell.getCellType() +"--"+cell.toString()+"--"+cell.getCachedFormulaResultType());
                }
                try { 
                    String url = "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=UTF-8"; 
                    Connection conn = DriverManager.getConnection(url,"mubawab","mubawab"); 
                    Statement st = conn.createStatement(); 
                    name = name.replaceAll("\'", " ");
                    if(row.getRowNum()>=3) {
                    	int total_punts = tirs_ficats*2+triples_ficats*3+lliures_ficats;
                    	float valoracio = tirs_ficats + triples_ficats + lliures_ficats + total_punts - tirs_intentats  
                    			- triples_intentats - lliures_intentats + rebot_of
                    			+ rebot_def + asis + rec - perd + taps_favor - taps_contra + faltes_rebudes - faltes_come;
                    	
                    st.executeUpdate("INSERT INTO `test`.`TEAM` (`grup_id`,`name`, `partidos`, `puntos`, `tirs_ficats`, "
                    		+ "`tirs_intentats`, `per_tir`, `triples_ficats`, `triples_intentats`, `per_triples`, "
                    		+ "`lliures_ficats`, `lliures_intentats`, `per_lliures`, `rebots_of`, `rebots_def`, "
                    		+ "`rebots_total`, `asist`, `recuperades`, `perdudes`, `taps_favor`, `taps_rebut`, "
                    		+ "`faltes_comeses`, `faltes_rebudes`, `valoracio`) \n" + 
                    		"VALUES ('"+grupId+"','"+name+"','"+partido+"','"+(total_punts)+"','"
                    		+tirs_ficats+"','"+tirs_intentats+"','"+(float)tirs_ficats*100/tirs_intentats+"','"
                    		+triples_ficats+"','"+triples_intentats+"','"+(float)triples_ficats*100/triples_intentats+"','"
                    		+lliures_ficats+"','"+lliures_intentats+"','"+(float)lliures_ficats*100/lliures_intentats+"','"
                    		+rebot_of+"','"+rebot_def+"','"+(rebot_of+rebot_def)+"','"+asis+"','"+rec+"','"
                    		+perd+"','"+taps_favor+"','"+taps_contra+"','"+faltes_come+"','"+faltes_rebudes+"','"+valoracio+"')");
                    }
                    conn.close(); 
                } catch (Exception e) { 
                    System.err.println("Got an exception! "); 
                    System.err.println(e.getMessage()); 
                } 
          
                System.out.println("");
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}