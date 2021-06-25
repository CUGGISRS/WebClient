package com.zd.earth.manage.util;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 本来写的是一个获取本机一些参数，作为源来进行加密的类，
 * 项目里只用到了硬盘编号，就没有用到这个类
 */
public class HardWareUtils   {

    public HardWareUtils(){
    }
    private static final long serialVersionUID = 1L;

    public static void main(String[] args){
        System.out.println("CPU  SN:" + HardWareUtils.getCPUSerial());//D0E9-8438
        System.out.println("主板  SN:" + HardWareUtils.getMotherboardSN());
        System.out.println("C盘   SN:" + HardWareUtils.getSerialNumber("c"));
        System.out.println(getLocalMac());
    }


    /**
     * 获得MAC地址（物理地址）
     */
    public static String getLocalMac() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            // 获取网卡，获取地址
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // 字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0" + str);
                } else {
                    sb.append(str);
                }
            }
            String localMAC = sb.toString().toUpperCase();
            return localMAC;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取主板序列号
     */
    public static String getMotherboardSN() {
        String result = "";
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_BaseBoard\") \n"
                    + "For Each objItem in colItems \n"
                    + "    Wscript.Echo objItem.SerialNumber \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }


    /**
     * 获得硬盘序列号
     * @return
     */
    public static String getHdSerialInfo() {

        String line = "";
        String HdSerial = "";//定义变量 硬盘序列号
        try {
            Process proces = Runtime.getRuntime().exec("cmd /c dir c:");//获取命令行参数
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(proces.getInputStream(),"gbk"));

            while ((line = buffreader.readLine()) != null) {
                if (line.indexOf("卷的序列号是 ") != -1) {  //读取参数并获取硬盘序列号

                    HdSerial = line.substring(line.indexOf("卷的序列号是 ") + "卷的序列号是 ".length());
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return HdSerial.trim();
    }

    /**
     * 获得某一硬盘的编号
     */
    public static String getSerialNumber(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("damn", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\""
                    + drive
                    + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber"; // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;

            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    /**
     * 获取CPU序列号
     */
    public static String getCPUSerial() {
        String result = "";
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_Processor\") \n"
                    + "For Each objItem in colItems \n"
                    + "    Wscript.Echo objItem.ProcessorId \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (result.trim().length() < 1 || result == null) {
            result = "无CPU_ID被读取";
        }
        return result.trim();
    }



}
