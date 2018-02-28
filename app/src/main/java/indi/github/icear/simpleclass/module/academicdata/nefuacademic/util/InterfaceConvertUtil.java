package indi.github.icear.simpleclass.module.academicdata.nefuacademic.util;

import java.util.ArrayList;
import java.util.List;

import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.academicdata.entity.IClassInfo;
import indi.github.icear.simpleclass.module.academicdata.entity.IUser;
import indi.github.icear.simpleclass.module.academicdata.nefuacademic.entity.NEFUClass;
import indi.github.icear.simpleclass.module.academicdata.nefuacademic.entity.NEFUClassInfo;
import indi.github.icear.simpleclass.module.academicdata.nefuacademic.entity.NEFUUser;

/**
 * Created by icear on 2018/2/28.
 * 将实现的实体集合转换为对应接口集合
 */

public class InterfaceConvertUtil {
    public static List<IClass> convertListForIClass(List<NEFUClass> list) {
        List<IClass> newList = new ArrayList<>();
        for (NEFUClass nefuClass : list) {
            newList.add(nefuClass);
        }
        return newList;
    }

    public static List<IClassInfo> convertListForIClassInfo(List<NEFUClassInfo> list) {
        List<IClassInfo> newList = new ArrayList<>();
        for (NEFUClassInfo nefuClassInfo : list) {
            newList.add(nefuClassInfo);
        }
        return newList;
    }

    public static List<IUser> convertListForIUser(List<NEFUUser> list) {
        List<IUser> newList = new ArrayList<>();
        for (NEFUUser nefuUser : list) {
            newList.add(nefuUser);
        }
        return newList;
    }
}
