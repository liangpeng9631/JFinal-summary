package com.app.util.tools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYin
{
    /** 
     * 将文字转为汉语拼音
    * @param chineselanguage 要转成拼音的中文
    */
   public String toHanyuPinyin(String ChineseLanguage)
   {
       char[] cl_chars = ChineseLanguage.trim().toCharArray();
       String hanyupinyin = "";
       HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
       defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出拼音全部小写
       defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
       defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V) ;
       try 
       {
         for (int i=0; i<cl_chars.length; i++)
         {
               if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+"))
               {
                   hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
               } 
               else 
               {
                   hanyupinyin += cl_chars[i];
               }
           }
       } 
       catch (BadHanyuPinyinOutputFormatCombination e)
       {
           System.out.println("字符不能转成汉语拼音");
       }
       return hanyupinyin;
   }
   
   /**
   * 取第一个汉字的第一个字符
  * @Title: getFirstLetter 
  * @Description: TODO 
  * @return String   
  * @throws
   */
   public  String getFirstLetter(String ChineseLanguage)
	{
	   
    	char[] cl_chars = ChineseLanguage.trim().toCharArray();
    	String hanyupinyin = "";
    	HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
    	defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 输出拼音全部大写
    	defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
    	try 
    		{
    			String str = String.valueOf(cl_chars[0]);
    			if (str.matches("[\u4e00-\u9fa5]+")) 
    			{
    				hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(
    				cl_chars[0], defaultFormat)[0].substring(0, 1);
    			} 
    			else if (str.matches("[0-9]+"))
    			{
    				hanyupinyin += cl_chars[0];
    			} 
    			else if (str.matches("[a-zA-Z]+")) 
    			{
    				hanyupinyin += cl_chars[0];
    			} 
    			else { }
    		} 
    		catch (BadHanyuPinyinOutputFormatCombination e)
    		{
    			System.out.println("字符不能转成汉语拼音");
    		}
    		return hanyupinyin;
	}
   
   
    public String getAbbreviationPinYin(String text )
    {
    	  String tempStr = "";
          for(int i=0; i<text.length(); i++) 
          {
              char c = text.charAt(i);
              if((int)c >= 33 && (int)c <=126)
               {
                  tempStr += String.valueOf(c);
               }
               else 
                {
                  tempStr += getPYChar( String.valueOf(c) );
                }
          }
          return tempStr;
    }
   
    /**
     * 取单个字符的拼音声母
     * @param c  //要转换的单个汉字
     * @return String 拼音声母
     */
    public static String getPYChar(String str)
    {	
    	if(null == str || str.equals("")) return "*";	

        byte[] array = str.getBytes();
        
        if(array.length < 2) return "*";
            
        int i = (short)(array[0] - '\0' + 256) * 256 + ((short)(array[1] - '\0' + 256));
        if ( i < 0xB0A1) return "*";
        if ( i < 0xB0C5) return "a";
        if ( i < 0xB2C1) return "b";
        if ( i < 0xB4EE) return "c";
        if ( i < 0xB6EA) return "d";
        if ( i < 0xB7A2) return "e";
        if ( i < 0xB8C1) return "f";
        if ( i < 0xB9FE) return "g";
        if ( i < 0xBBF7) return "h";
        if ( i < 0xBFA6) return "j";
        if ( i < 0xC0AC) return "k";
        if ( i < 0xC2E8) return "l";
        if ( i < 0xC4C3) return "m";
        if ( i < 0xC5B6) return "n";
        if ( i < 0xC5BE) return "o";
        if ( i < 0xC6DA) return "p";
        if ( i < 0xC8BB) return "q";
        if ( i < 0xC8F6) return "r";
        if ( i < 0xCBFA) return "s";
        if ( i < 0xCDDA) return "t";
        if ( i < 0xCEF4) return "w";
        if ( i < 0xD1B9) return "x";
        if ( i < 0xD4D1) return "y";
        if ( i < 0xD7FA) return "z";
        return "*";

    }  
}