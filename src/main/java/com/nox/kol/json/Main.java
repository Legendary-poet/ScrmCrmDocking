//package com.nox.kol.json;
//
//
//import java.io.BufferedWriter;
//import java.io.FileOutputStream;
//import java.io.OutputStreamWriter;
//
//public class Main {
//    public static void Main(String[] args) throws Exception {
//
//        String filePath = "D:\\a.txt";
//        FileOutputStream fileOutputStream = null;
//        OutputStreamWriter outputStreamWriter = null;
//        BufferedWriter bufferedWriter = null;
//        try {
//            fileOutputStream = new FileOutputStream(filePath);
//            outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
//            bufferedWriter = new BufferedWriter(outputStreamWriter);
//            for (int j=0;j<list.size();j++) {
//                bufferedWriter.write(res);
//                if (i < students.length - 1) {
//                    bufferedWriter.newLine();
//                }
//            }
//        } finally {
//            if (bufferedWriter != null) {
//                bufferedWriter.close();
//            }
//            if (outputStreamWriter != null) {
//                outputStreamWriter.close();
//            }
//            if (fileOutputStream != null) {
//                fileOutputStream.close();
//            }
//        }
//    }
//}