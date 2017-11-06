package org.benmobile.core.natvie.ui.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * @author unkown
 * @marker liukun@tomstaff.com
 * @since 2016-4-7
 * @category
 */
public class ResultViewModle implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** The result code **/
	public String returnCode;
	/** The results described **/
	public String returnMessage;
	/** The results described **/
	public String returnMsg;
	/** ? **/
	public String workStatus;
	/** ? **/
	public String url;
	/** Submit an application for cancellation of the order of h5**/
	public String toHtml5URL;
	/** 1: jump H5; Zero: directly cance**/
	public String isMerAuditFlag;

	public ResultViewModle(JSONObject jsn) throws JSONException
	{
		if (jsn.has("returnCode"))
		{
			this.returnCode = jsn.getString("returnCode");
		}
		if (jsn.has("returnMessage"))
		{
			this.returnMessage = jsn.getString("returnMessage");
		}
		if (jsn.has("returnMsg"))
		{
			this.returnMsg = jsn.getString("returnMsg");
		}
		if (jsn.has("workStatus"))
		{
			this.workStatus = jsn.getString("workStatus");
		}
		if (jsn.has("url"))
		{
			this.url = jsn.getString("url");
		}
		if (jsn.has("toHtml5URL"))
		{
			this.toHtml5URL = jsn.getString("toHtml5URL");
		}
		if (jsn.has("isMerAuditFlag"))
		{
			this.isMerAuditFlag = jsn.getString("isMerAuditFlag");
		}
	}

	public ResultViewModle()
	{
		returnCode = "-1";
		returnMessage = "";
		returnMsg = "";
	}



	public static String filterhtml(String input)
	{
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");

		return str;
	}

	public static boolean jsonIsNull(String key, JSONObject jsn) throws JSONException
	{
		return jsn.has(key) && jsn.getString(key) != null && !jsn.getString(key).trim().equals("null");

	}
}
