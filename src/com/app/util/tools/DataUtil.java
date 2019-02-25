package com.app.util.tools;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 数据转换工具类，可以从Object转换成需要数据类型
 *
 * @author seven.wha
 */
public class DataUtil {
    /**
     * 对象空判断
     *
     * @param _obj
     * @return
     */
	@SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object _obj) 
    {
        if (_obj == null) 
        {
            return true;
        } 
        else if (_obj instanceof String) 
        {
            return "null".equals(((String) _obj).toLowerCase()) ? false : "".equals(((String) _obj).trim());
        } 
        else if (_obj instanceof Collection)
        {
            Collection map1 = (Collection) _obj;
            return map1.size() == 0;
        } 
        else if (_obj instanceof Map)
        {
			Map map = (Map) _obj;
            return map.size() == 0;
        } else {
            return _obj.getClass().isArray() ? Array.getLength(_obj) == 0 : false;
        }
    }

    /**
     * 对象是数字
     *
     * @param _obj
     * @return
     */
    public static boolean isNumber(Object _obj)
    {
        String zs = "^-?\\d+$";//整数
        String fd = "^(-?\\d+)(\\.\\d+)?$";//浮点数
        if (!isEmpty(_obj) && (_obj.toString().matches(zs) || _obj.toString().matches(fd)))
        {
            return true;
        }
        return false;
    }

    public static String getString(Object obj, String _default) throws Exception
    {
        return isEmpty(obj) ? _default : obj.toString();
    }

    public static Long getLong(Object obj, Long _default) throws Exception
    {
        return isEmpty(obj) ? _default : Long.parseLong(String.valueOf(obj));
    }

    public static Double getDouble(Object obj, Double _default) throws Exception
    {
        return isEmpty(obj) ? _default : Double.parseDouble(String.valueOf(obj));
    }

    public static int getInt(Object obj, int _default) throws Exception
    {
        return isEmpty(obj) ? _default : Integer.parseInt(String.valueOf(obj));
    }

    public static float getFloat(Object obj, float _default) throws Exception
    {
        return isEmpty(obj) ? _default : Float.parseFloat(String.valueOf(obj));
    }

    public static Date getDate(Object obj, String _format, Date _default) throws Exception
    {
        _format = isEmpty(_format) ? "yyyy-MM-dd HH:mm:ss" : _format;
        return isEmpty(obj) ? _default : (Date) new SimpleDateFormat(_format).parseObject(String.valueOf(obj));
    }

    public static BigDecimal getBigDecimal(Object obj, BigDecimal _default) throws Exception 
    {
        return isEmpty(obj) ? _default : BigDecimal.valueOf(getDouble(obj, isEmpty(_default) ? null : _default.doubleValue()));
    }

    public static boolean getBoolean(Object obj, boolean _default) throws Exception 
    {
        if (!(obj instanceof Boolean)) 
        {
            obj = "Y".equals(DataUtil.getString(obj, "F")) ? true : false;
        }
        return isEmpty(obj) ? _default : Boolean.valueOf(String.valueOf(obj));
    }
}
