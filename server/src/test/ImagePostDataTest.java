package test;

import static org.junit.Assert.*;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.junit.Before;
import org.junit.Test;

import ImagePostData.ImagePostData;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class ImagePostDataTest {

	@Test
	public void testSetGetImageItem() {
		ImagePostData imp = new ImagePostData();
		org.apache.commons.fileupload.FileItem immock = mock(org.apache.commons.fileupload.FileItem.class);;
		imp.setImageItem(immock);
		assertEquals(immock, imp.getImageItem());
	}

	@Test
	public void testSetGetJSONdata() {
		ImagePostData imp = new ImagePostData();
		imp.setJSONdata("{}");
		assertEquals(imp.getJSONdata(), "{}");
	}

	@Test
	public void testExtract() {
		ImagePostData imp = new ImagePostData();
		List<Object> l = new ArrayList<Object>();
		
		org.apache.commons.fileupload.FileItem jsonmock = mock(org.apache.commons.fileupload.FileItem.class);
		when(jsonmock.isFormField()).thenReturn(true);
		when(jsonmock.getFieldName()).thenReturn("jsonData");
		l.add(jsonmock);
		
		org.apache.commons.fileupload.FileItem filemock = mock(org.apache.commons.fileupload.FileItem.class);
		when(filemock.isFormField()).thenReturn(false);
		when(filemock.getFieldName()).thenReturn("image");
		l.add(filemock);
		
		imp.extract(l);
	}

}
