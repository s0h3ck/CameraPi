package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.sqlite.core.DB;

import libdb.Image;
import libdb.SQLiteJDBC;
import servlets.CameraPage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

public class PagesTest {
	
	private CameraPage getMockedInstance(){
		CameraPage camPage = new CameraPage();

		camPage.setSocketServer(mock(libcom.CameraPiSocketServer.class));
		camPage.setTesting(true);
		camPage.init();
		camPage.setDb(mock(SQLiteJDBC.class));
		
		return camPage;
	}
	
	@Test
	public void testDoGetClick() throws ServletException, IOException {
		CameraPage camPage = getMockedInstance();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponseWrapper.class);
		
		//when(camPage.isDBStarted()).thenReturn(true);
		
		when(request.getParameter("action")).thenReturn("click");
		when(request.getParameter("btn")).thenReturn("8");
		
		camPage.doGet(request, response);	
		
		camPage.destroy();
	}
	
	@Test
	public void testDoGetMVLEFT() throws ServletException, IOException {
		CameraPage camPage = getMockedInstance();
		
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponseWrapper.class);
		
		when(request.getParameter("action")).thenReturn("mv-left");
		when(request.getParameter("btn")).thenReturn("8");
		
		camPage.doGet(request, response);	
	}
	
	@Test
	public void testDoGetMVRight() throws ServletException, IOException {
		CameraPage camPage = getMockedInstance();
		
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponseWrapper.class);
		
		when(request.getParameter("action")).thenReturn("mv-right");
		when(request.getParameter("btn")).thenReturn("8");
		
		camPage.doGet(request, response);	
	}
	
	@Test
	public void testDoGetNull() throws ServletException, IOException {
		CameraPage camPage = getMockedInstance();
		
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponseWrapper.class);
		
		when(request.getParameter("action")).thenReturn(null);
		when(request.getParameter("btn")).thenReturn("8");
		
		camPage.doGet(request, response);	
	}
	
	@Test
	public void testDoGetGETLAST() throws ServletException, IOException {
		CameraPage camPage = getMockedInstance();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponseWrapper.class);
		
		when(request.getParameter("action")).thenReturn("get-last");
		when(request.getParameter("btn")).thenReturn("8");
		
		camPage.doGet(request, response);	
	}
	
	@Test
	public void testGetStartOfDate() {
		CameraPage camPage = getMockedInstance();
		Date d = camPage.getStartOfDate(new Date());
		assertTrue(d.before(new Date()));
	}

	@Test
	public void testGetEndOfDate() {
		CameraPage camPage = getMockedInstance();
		Date d = camPage.getEndOfDate(new Date());
		assertTrue(d.after(new Date()));
	}

	@Test
	public void testGetDatePictures() {
		CameraPage camPage = getMockedInstance();
		JSONArray pics = camPage.getDatePictures(new Date());
		// Empty json array because DB is mocked
		System.out.println(pics);
	}

	@Test
	public void testBuildImageObj() {
		CameraPage camPage = getMockedInstance();
		Image immock = mock(Image.class);
		
		when(immock.getFileName()).thenReturn("testname");
		when(immock.getJSONData()).thenReturn("{}");
		when(immock.getReceiveDate()).thenReturn(new Date(100020020));
		
		JSONObject pic = camPage.buildImageObj(immock);
		
		assertEquals(pic.getLong("date"), 100020020);
		assertEquals(pic.getString("filename"), "testname");
		assertEquals(pic.getJSONObject("jsonData").toString(), "{}");
	}	
}
