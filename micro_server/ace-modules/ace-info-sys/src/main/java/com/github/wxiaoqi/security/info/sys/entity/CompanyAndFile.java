package com.github.wxiaoqi.security.info.sys.entity;

import lombok.Data;

import java.util.List;

@Data
public class CompanyAndFile extends CompanyInfo {

    List<FileInfo> ImgLists;

}
