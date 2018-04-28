package stan.boxes.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

public class JSONWriter
{
	static public String write(Map map)
	{
		StringWriter writer = new StringWriter();
		try
		{
			mapToJSONString(map, writer);
			return writer.toString();
		}
		catch(IOException e)
		{
			throw new ParseException(-1, -1, map);
		}
	}
	
	static private void mapToJSONString(Map map, Writer out)
		throws IOException
	{
		if(map == null)
		{
			out.write("null");
			return;
		}
		boolean first = true;
		Iterator iter=map.entrySet().iterator();
        out.write('{');
		while(iter.hasNext())
		{
            if(first)
                first = false;
            else
                out.write(',');
			Map.Entry entry=(Map.Entry)iter.next();
            out.write('\"');
            out.write(escape(String.valueOf(entry.getKey())));
            out.write('\"');
            out.write(':');
			objectToJSONString(entry.getValue(), out);
		}
		out.write('}');
	}
	static private void objectToJSONString(Object value, Writer out)
		throws IOException
	{
		if(value == null){
			out.write("null");
            return;
		}
		if(value instanceof String){		
            out.write('\"');
			out.write(escape((String)value));
            out.write('\"');
			return;
		}
		if(value instanceof Double){
			if(((Double)value).isInfinite() || ((Double)value).isNaN())
				out.write("null");
			else
				out.write(value.toString());
			return;
		}
		if(value instanceof Float){
			if(((Float)value).isInfinite() || ((Float)value).isNaN())
				out.write("null");
			else
				out.write(value.toString());
			return;
		}
		if(value instanceof Number){
			out.write(value.toString());
			return;
		}
		if(value instanceof Boolean){
			out.write(value.toString());
			return;
		}
		if(value instanceof Map){
			mapToJSONString((Map)value, out);
			return;
		}
		if(value instanceof Collection){
			arrayToJSONString((Collection)value, out);
            return;
		}
		if(value instanceof byte[]){
			List<Byte> list = new ArrayList<Byte>();
			for (byte val : (byte[]) value)
			{
				list.add(val);
			}
			arrayToJSONString(list, out);
			return;
		}
		if(value instanceof short[]){
			List<Short> list = new ArrayList<Short>();
			for (short val : (short[]) value)
			{
				list.add(val);
			}
			arrayToJSONString(list, out);
			return;
		}
		if(value instanceof int[]){
			List<Integer> list = new ArrayList<Integer>();
			for (int val : (int[]) value)
			{
				list.add(val);
			}
			arrayToJSONString(list, out);
			return;
		}
		if(value instanceof long[]){
			List<Long> list = new ArrayList<Long>();
			for (long val : (long[]) value)
			{
				list.add(val);
			}
			arrayToJSONString(list, out);
			return;
		}
		if(value instanceof float[]){
			List<Float> list = new ArrayList<Float>();
			for (float val : (float[]) value)
			{
				list.add(val);
			}
			arrayToJSONString(list, out);
			return;
		}
		if(value instanceof double[]){
			List<Double> list = new ArrayList<Double>();
			for (double val : (double[]) value)
			{
				list.add(val);
			}
			arrayToJSONString(list, out);
			return;
		}
		if(value instanceof char[]){
			List<Character> list = new ArrayList<Character>();
			for (char val : (char[]) value)
			{
				list.add(val);
			}
			arrayToJSONString(list, out);
			return;
		}
		if(value instanceof Object[]){
			List<Object> list = new ArrayList<Object>();
			Collections.addAll(list, (Object[]) value);
			arrayToJSONString(list, out);
			return;
		}
		if(value instanceof boolean[]){
			List<Boolean> list = new ArrayList<Boolean>();
			for (boolean val : (boolean[]) value)
			{
				list.add(val);
			}
			arrayToJSONString(list, out);
			return;
		}
		objectToJSONString(value.toString(), out);
	}
	static private void arrayToJSONString(Collection collection, Writer out)
		throws IOException
	{
		if(collection == null)
		{
			out.write("null");
			return;
		}
		boolean first = true;
		Iterator iter=collection.iterator();
        out.write('[');
		while(iter.hasNext())
		{
            if(first)
                first = false;
            else
                out.write(',');
			Object value=iter.next();
			if(value == null)
			{
				out.write("null");
				continue;
			}
			objectToJSONString(value, out);
		}
		out.write(']');
	}
    static private String escape(String s)
	{
        StringBuffer sb = new StringBuffer();
    	final int len = s.length();
		for(int i=0;i<len;i++)
		{
			char ch=s.charAt(i);
			switch(ch){
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')){
					String ss=Integer.toHexString(ch);
					sb.append("\\u");
					for(int k=0;k<4-ss.length();k++){
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				}
				else
				{
					sb.append(ch);
				}
			}
		}
        return sb.toString();
	}
}