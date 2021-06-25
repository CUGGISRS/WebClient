package com.github.wxiaoqi.security.gyx.sys.entity;

import lombok.Data;

import java.util.List;

@Data
public class SupplyDemandAndFile extends SupplyDemandInfo {

    List<FileInfo> ImgLists;

}
