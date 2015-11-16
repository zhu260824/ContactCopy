package com.zhulin.contactcopy.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

import com.zhulin.contactcopy.paser.HXPhone;

public class ContactsUtil {
		//读取通讯录的全部的联系人
		//需要先在raw_contact表中遍历id，并根据id到data表中获取数据
		public static List<HXPhone> ReadAll(Context context){
			//uri = content://com.android.contacts/contacts
			List<HXPhone> list=new ArrayList<HXPhone>();
			Uri uri = Uri.parse("content://com.android.contacts/contacts");	//访问raw_contacts表
			ContentResolver resolver = context.getContentResolver();
			Cursor cursor = resolver.query(uri, new String[]{Data._ID}, null, null, null);	//获得_id属性
			while(cursor.moveToNext()){
				HXPhone contacts=new HXPhone();
				int id = cursor.getInt(0);//获得id并且在data中寻找数据
				contacts.id=String.valueOf(id);
				uri = Uri.parse("content://com.android.contacts/contacts/"+id+"/data");	//如果要获得data表中某个id对应的数据，则URI为content://com.android.contacts/contacts/#/data
				Cursor cursor2 = resolver.query(uri, new String[]{Data.DATA1,Data.MIMETYPE}, null,null, null);	//data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
				while(cursor2.moveToNext()){
					String data = cursor2.getString(cursor2.getColumnIndex("data1"));
					if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")){		//如果是名字
						contacts.name=data;
					}
					else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/phone_v2")){	//如果是电话
						contacts.phone=data;
					}
				}
				list.add(contacts);
			}
			cursor.close();
			return list;
		}
		
		//根据电话号码查询姓名（在一个电话打过来时，如果此电话在通讯录中，则显示姓名）
		public static String ReadNameByPhone(Context context,String phone){
			//uri=  content://com.android.contacts/data/phones/filter/#
			Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/"+phone);	
			ContentResolver resolver = context.getContentResolver();
			Cursor cursor = resolver.query(uri, new String[]{Data.DISPLAY_NAME}, null, null, null);	//从raw_contact表中返回display_name
			if(cursor.moveToFirst()){
				return cursor.getString(0);
			}
			return null;
			
		}
		
		public static void AddContacts(Context context,String name,String phone){
			//插入raw_contacts表，并获取_id属性
			Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
			ContentResolver resolver =context.getContentResolver();
			ContentValues values = new ContentValues();
			long contact_id = ContentUris.parseId(resolver.insert(uri, values));
			//插入data表
			uri = Uri.parse("content://com.android.contacts/data");
			//add Name
			values.put("raw_contact_id", contact_id);
			values.put(Data.MIMETYPE,"vnd.android.cursor.item/name");
			values.put("data2", name);
			values.put("data1", name);
			resolver.insert(uri, values);
			values.clear();
			//add Phone
			values.put("raw_contact_id", contact_id);
			values.put(Data.MIMETYPE,"vnd.android.cursor.item/phone_v2");
			values.put("data2", phone);	//手机
			values.put("data1", phone);
			resolver.insert(uri, values);
			resolver.insert(uri, values);
		}
		
		
		public static void DeleteContacts(Context context,String name)throws Exception{
			
			//根据姓名求id
			Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
			ContentResolver resolver = context.getContentResolver();
			Cursor cursor = resolver.query(uri, new String[]{Data._ID},"display_name=?", new String[]{name}, null);
			if(cursor.moveToFirst()){
				int id = cursor.getInt(0);
				//根据id删除data中的相应数据
				resolver.delete(uri, "display_name=?", new String[]{name});
				uri = Uri.parse("content://com.android.contacts/data");
				resolver.delete(uri, "raw_contact_id=?", new String[]{id+""});
			}
		}
		
		public static void UpdateContacts(Context context,String content ,int id)throws Exception{
			ContentValues values = new ContentValues();
			values.put(Phone.NUMBER,content);
			context.getContentResolver().update(android.provider.ContactsContract.Data.CONTENT_URI, values, " raw_contact_id=? and mimetype=? and data2=?", new String[]{String.valueOf(id),Phone.CONTENT_ITEM_TYPE,"2"});
			values.clear();


		}
}
