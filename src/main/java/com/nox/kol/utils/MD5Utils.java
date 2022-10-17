package com.nox.kol.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Utils {
    private static char[] hexDigitsLower = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static char[] hexDigitsUpper = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String generateMD5(String value){
        byte[] btInput=value.getBytes();
        return generateMD5(btInput);
    }

    public static String generateMD5(byte[] btInput){
        try{
            return generateMD5Detail(btInput,hexDigitsLower);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String generateMD5_16(String value){
        return generateMD5(value).substring(8, 24);
    }

    public static String generateMD5Upper(String value){
        try{
            byte[] btInput=value.getBytes();
            return generateMD5Detail(btInput,hexDigitsUpper);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private static String generateMD5Detail(byte[] btInput,char[] chars)throws Exception{
        MessageDigest mdInst=MessageDigest.getInstance("MD5");
        mdInst.update(btInput);
        byte[] md=mdInst.digest();
        return resultBytesToString(md,chars);
    }
    private static String resultBytesToString(byte[] md,char[] chars){
        int j=md.length;
        char[] str = new char[j * 2];
        int k=0;
        for (byte byte0 : md) {
            str[k++] = chars[byte0 >>> 4 & 0xf];
            str[k++] = chars[byte0 & 0xf];
        }
        return new String(str);
    }

    public static String resultBytesToString(byte[] md) {
        return resultBytesToString(md,hexDigitsLower);
    }

    public static String getFileMD5(File file){
        String value=null;
        FileInputStream fis=null;
        try{
            MessageDigest md5=MessageDigest.getInstance("MD5");
            fis=new FileInputStream(file);
            updateByteToMd5(fis,md5);
            BigInteger bi=new BigInteger(1, md5.digest());
            value=bi.toString(16);
            if(value.length()!=32){
                value="00000000000000000000000000000000".substring(0, 32-value.length())+value;
            }
            return value;
        }catch(Exception ex){
            return value;
        }finally{
            if(fis!=null){
                try{
                    fis.close();
                }catch(Exception ignored){

                }
            }
        }
    }

    public static byte[] getFileMD5Bytes(File file) {
        FileInputStream fis=null;
        try{
            MessageDigest md5=MessageDigest.getInstance("MD5");
            fis=new FileInputStream(file);
            updateByteToMd5(fis,md5);
            return md5.digest();
        }catch(Exception ex){
            return null;
        }finally{
            if(fis!=null){
                try{
                    fis.close();
                }catch(Exception ignored){

                }
            }
        }
    }
    public static byte[] getByteMD5Bytes(byte[] bytes) {
        try{
            MessageDigest md5=MessageDigest.getInstance("MD5");
            md5.update(bytes);
            return md5.digest();
        }catch(Exception ex){
            return null;
        }
    }
    public static String getBytesMD5(byte[] bytes) {
        try{
            MessageDigest md5=MessageDigest.getInstance("MD5");
            md5.update(bytes);
            return resultBytesToString(md5.digest());
        }catch(Exception ex){
            return null;
        }
    }

    public static String getFileMD5(String filePath,long offset,long size){
        RandomAccessFile raf=null;
        try{
            MessageDigest md5=MessageDigest.getInstance("MD5");
            raf=new RandomAccessFile(filePath, "r");
            raf.seek(offset);
            byte[] bs=new byte[4096];
            int n=0;
            do{
                int readSize=size>4096?4096:(int)size;
                if((n=raf.read(bs, 0, readSize))!=-1){
                    md5.update(bs, 0, n);
                    size-=n;
                }else{
                    break;
                }
            }while(size>0);
            return md5DigestToString32(md5);
        }catch(Exception ex){
            return "";
        }finally{
            if(raf!=null){
                try{
                    raf.close();
                }catch(Exception ignored){

                }
            }
        }
    }
    private static String md5DigestToString32(MessageDigest md5){
        BigInteger bi=new BigInteger(1, md5.digest());
        String value=bi.toString(16);
        if(value.length()!=32){
            value="00000000000000000000000000000000".substring(0, 32-value.length())+value;
        }
        return value;
    }

    public static String getInputStreamMd5(InputStream inputStream){
        String value="";
        try{
            MessageDigest md5=MessageDigest.getInstance("MD5");
            updateByteToMd5(inputStream,md5);
            return md5DigestToString32(md5);
        }catch(Exception ex){
            return value;
        }finally{
            if(inputStream!=null){
                try{
                    inputStream.close();
                }catch(Exception ignored){

                }
            }
        }
    }
    private static void updateByteToMd5(InputStream inputStream,MessageDigest md5)throws Exception{
        int n=0;
        byte[] bs=new byte[4096];
        while((n=inputStream.read(bs))!=-1){
            md5.update(bs, 0, n);
        }
    }
}

