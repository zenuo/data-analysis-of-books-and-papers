package edu.libsys.transform;

import java.io.*;

/**
 * 生成单列批量更新的SQL语句
 * 参考http://www.ghugo.com/update-multiple-rows-with-different-values-and-a-single-sql-query/
 */
public class GenerateUpdateSql {

    private static final int AMOUNT_OF_RECORDS = 473432;
    private static final int COLUMN_INDEX = 1;
    private static final int RECORD_PER_ROW_SQL = 10000;
    private static final String COLUMN_DELIMITER = "#";
    private static final String INDEX_NAME = "id";
    private static final String COLUMN_NAME = "author";
    private static final String DATABASE_NAME = "LIBSYS";
    private static final String TABLE_NAME = "PAPER";
    private static final String SOURCE_FILE_PATH = "/home/yuanzhen/project/data/txt/paperInfo.txt";
    private static final String SQL_FILE_PATH = "/home/yuanzhen/paperInfo-author.sql";

    /**
     * 主方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {

        String[] indexArray01 = new String[RECORD_PER_ROW_SQL];
        String[] valueArray01 = new String[RECORD_PER_ROW_SQL];

        try {
            BufferedReader sourceFileBR = new BufferedReader(new InputStreamReader(new FileInputStream(SOURCE_FILE_PATH), "UTF-8"));
            BufferedWriter sqlFileBW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(SQL_FILE_PATH, false), "UTF-8"));
            sqlFileBW.write(String.format("USE %s;\n", DATABASE_NAME));
            sqlFileBW.flush();
            String line;
            int index = 0;
            int amountOfLeftRecords = AMOUNT_OF_RECORDS;
            while ((line = sourceFileBR.readLine()) != null) {
                String[] tokens = line.split(COLUMN_DELIMITER);
                indexArray01[index] = tokens[0];
                valueArray01[index] = tokens[COLUMN_INDEX];

                index++;
                if (index == RECORD_PER_ROW_SQL) {
                    sqlFileBW.write(parseSql(indexArray01, valueArray01));
                    sqlFileBW.flush();
                    //复位
                    index = 0;
                    //计算剩下的记录数量
                    amountOfLeftRecords -= RECORD_PER_ROW_SQL;
                    if (amountOfLeftRecords < RECORD_PER_ROW_SQL) {
                        break;
                    }
                }
            }

            String[] indexArray02 = new String[amountOfLeftRecords];
            String[] valueArray02 = new String[amountOfLeftRecords];

            while ((line = sourceFileBR.readLine()) != null) {
                String[] tokens = line.split(COLUMN_DELIMITER);
                indexArray02[index] = tokens[0];
                valueArray02[index] = tokens[COLUMN_INDEX];
                index++;

                if (index == amountOfLeftRecords) {
                    sqlFileBW.write(parseSql(indexArray02, valueArray02));
                    sqlFileBW.flush();
                    index = 0;
                }
            }

            sourceFileBR.close();
            sqlFileBW.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("OK, bye.\n");
    }

    /**
     * 构造sql语句
     *
     * @param indexArray 索引值的数组
     * @param valueArray 数据值的数组
     * @return sql语句
     */
    private static String parseSql(String[] indexArray, String[] valueArray) {
        //UPDATE BOOK SET callName=CASE id WHEN 1 THEN '1' WHEN 2 THEN '2' WHEN 3 THEN '3' END WHERE id IN (1,2,3);
        StringBuilder indexBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        //考虑到逗号问题，将第一条记录在循环体外加入StringBuilder
        indexBuilder.append(indexArray[0]);
        valueBuilder.append(String.format("WHEN %s THEN %s", indexArray[0], valueArray[0]));

        for (int i = 1; i < indexArray.length; i++) {
            indexBuilder.append(String.format(",%s", indexArray[i]));
            valueBuilder.append(String.format(" WHEN %s THEN %s", indexArray[i], valueArray[i]));
        }
        return String.format("START TRANSACTION;UPDATE %s SET %s=CASE %s %s END WHERE %s IN (%s);COMMIT;\n", TABLE_NAME, COLUMN_NAME, INDEX_NAME, valueBuilder.toString(), INDEX_NAME, indexBuilder.toString());
    }
}
