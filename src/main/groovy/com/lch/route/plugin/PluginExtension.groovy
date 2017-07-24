package com.lch.route.plugin

class PluginExtension {

  public List<String> visitors =[]

  public def isNeedModifyJar={jarAbsPath->return true}

  public boolean isNeedModifyDirClasses=true


}