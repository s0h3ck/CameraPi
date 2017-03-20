package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.junit.Test;

import servlets.UploadServlet;

public class UploadServletTest {
	@Test
	public void testDoPost() throws IOException{
		UploadServlet test = new UploadServlet();
		test.setTesting(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		File f = new File("testpost.txt");
		
		PrintWriter pw = new PrintWriter(f);
		
		when(response.getWriter()).thenReturn(pw);

		test.doPost(request, response);
		
		pw.close();
		
		String content = null;
		Scanner sc = new Scanner(f);
		// get last line
		while(sc.hasNextLine()){
			content = sc.nextLine();
		}
		
		assertEquals(content, "File upload success");
	}
	
	@Test
	public void testDoPostFail() throws IOException{
		UploadServlet test = new UploadServlet();
		test.setTesting(false);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		File f = new File("testpost.txt");
		
		PrintWriter pw = new PrintWriter(f);
		
		when(response.getWriter()).thenReturn(pw);

		test.doPost(request, response);
		
		pw.close();
		
		String content = null;
		Scanner sc = new Scanner(f);
		// get last line
		while(sc.hasNextLine()){
			content = sc.nextLine();
		}
		
		assertEquals(content, "File upload failed");
	}
	
	@Test
	public void testSetUploadFileName() 
	{
		UploadServlet test = new UploadServlet();
		test.initializeUploadPath();
		String uploadPath = ".";
		String fileName = "date-here";
		String testFileName = test.generateUploadFilePath(uploadPath, fileName);
		assertTrue(testFileName.contains("date-here"));
	}
	
	@Test
	public void testCreateUploadDirectory() 
	{
		UploadServlet test = new UploadServlet();
		String uploadPath = "./test-upload-dir";
		File testDir = test.CreateUploadDirectory(uploadPath);
		
		if(!testDir.exists())
		{
			fail("Directory does not exist");
		}
		testDir.delete();
	}
	
	@Test
	public void testGenerateFileDate() {
		UploadServlet test = new UploadServlet();
		Date d = test.GenerateFileDate();
		assert(d.compareTo(new Date()) <= 0);
	}

	@Test
	public void testFormatDateAsFileName() {
		UploadServlet test = new UploadServlet();
		Date d = test.GenerateFileDate();
		String fileName = test.FormatDateAsFileName(d);
		assert(fileName.contains(".jpg"));
		assert(fileName.contains("_"));
	}
	
	@Test
	public void testUploadPathGettersSetters() {
		UploadServlet test = new UploadServlet();
		test.setUploadPath("testpath123/a");
		assert(test.getUploadPath().equals("testpath123/a"));
	}
	@Test
	public void testSaveImage() {
		UploadServlet test = new UploadServlet();
		FileItem mockfileitem = mock(FileItem.class);
		
		test.saveImage(mockfileitem, "{test:'test'}");
		assertEquals(test.getDB().selectLast().getJSONData(), "{test:'test'}");
	}
}
