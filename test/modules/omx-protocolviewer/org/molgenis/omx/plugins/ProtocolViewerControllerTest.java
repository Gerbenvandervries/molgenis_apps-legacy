package org.molgenis.omx.plugins;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.codec.digest.DigestUtils;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.server.MolgenisRequest;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.observ.Category;
import org.molgenis.observ.DataSet;
import org.molgenis.observ.ObservableFeature;
import org.molgenis.observ.Protocol;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.Test;

public class ProtocolViewerControllerTest
{

	@Test
	public void handleRequest_getDataset_protocol() throws Exception
	{
		// mock db
		Database db = mock(Database.class);

		DataSet dataSet = new DataSet();
		dataSet.setId(1);
		dataSet.setName("dataset");
		dataSet.setProtocolUsed(2);

		Protocol protocol = new Protocol();
		protocol.setId(2);
		protocol.setName("protocol");
		protocol.setFeatures_Id(Arrays.asList(3, 4));

		ObservableFeature feature1 = new ObservableFeature();
		feature1.setId(3);
		feature1.setName("feature1");

		ObservableFeature feature2 = new ObservableFeature();
		feature2.setId(4);
		feature2.setName("feature2");

		when(db.find(DataSet.class, new QueryRule(DataSet.ID, Operator.EQUALS, 1))).thenReturn(
				Collections.singletonList(dataSet));
		when(db.find(Protocol.class, new QueryRule(Protocol.ID, Operator.EQUALS, 2))).thenReturn(
				Collections.singletonList(protocol));
		when(db.find(ObservableFeature.class, new QueryRule(ObservableFeature.ID, Operator.IN, Arrays.asList(3, 4))))
				.thenReturn(Arrays.asList(feature1, feature2));

		ProtocolViewerController controller = new ProtocolViewerController("test", mock(ScreenController.class));

		// mock request
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest("GET", null);
		httpServletRequest.setParameter("datasetid", "1");
		httpServletRequest.setParameter("__action", "download_json_getdataset");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			controller.handleRequest(db, new MolgenisRequest(httpServletRequest), bos);
			String output = new String(bos.toByteArray(), Charset.forName("UTF-8"));

			String expected = "{\"id\":1,\"name\":\"dataset\",\"protocol\":{\"id\":2,\"name\":\"protocol\",\"features\":[{\"id\":3,\"name\":\"feature1\",\"dataType\":\"string\"},{\"id\":4,\"name\":\"feature2\",\"dataType\":\"string\"}]}}";
			assertEquals(output, expected);
		}
		finally
		{
			bos.close();
		}
	}

	@Test
	public void handleRequest_getDataset_noProtocol() throws Exception
	{
		// mock db
		Database db = mock(Database.class);

		DataSet dataSet = new DataSet();
		dataSet.setId(1);
		dataSet.setName("dataset");

		when(db.find(DataSet.class, new QueryRule(DataSet.ID, Operator.EQUALS, 1))).thenReturn(
				Collections.singletonList(dataSet));

		ProtocolViewerController controller = new ProtocolViewerController("test", mock(ScreenController.class));

		// mock request
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest("GET", null);
		httpServletRequest.setParameter("datasetid", "1");
		httpServletRequest.setParameter("__action", "download_json_getdataset");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			controller.handleRequest(db, new MolgenisRequest(httpServletRequest), bos);
			String output = new String(bos.toByteArray(), Charset.forName("UTF-8"));

			String expected = "{\"id\":1,\"name\":\"dataset\"}";
			assertEquals(output, expected);
		}
		finally
		{
			bos.close();
		}
	}

	@Test
	public void handleRequest_getFeature_category() throws Exception
	{
		// mock db
		Database db = mock(Database.class);

		ObservableFeature feature = new ObservableFeature();
		feature.setId(1);
		feature.setName("feature");
		feature.setIdentifier("featureid1");

		Category category1 = new Category();
		category1.setId(2);
		category1.setValueCode("code1");
		category1.setName("label1");
		category1.setDescription("description1");

		Category category2 = new Category();
		category2.setId(2);
		category2.setValueCode("code2");
		category2.setName("label2");
		category2.setDescription("description2");

		when(db.find(ObservableFeature.class, new QueryRule(ObservableFeature.ID, Operator.EQUALS, 1))).thenReturn(
				Collections.singletonList(feature));
		when(
				db.find(Category.class, new QueryRule(Category.OBSERVABLEFEATURE_IDENTIFIER, Operator.EQUALS,
						"featureid1"))).thenReturn(Arrays.asList(category1, category2));

		ProtocolViewerController controller = new ProtocolViewerController("test", mock(ScreenController.class));

		// mock request
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest("GET", null);
		httpServletRequest.setParameter("featureid", "1");
		httpServletRequest.setParameter("__action", "download_json_getfeature");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			controller.handleRequest(db, new MolgenisRequest(httpServletRequest), bos);
			String output = new String(bos.toByteArray(), Charset.forName("UTF-8"));

			String expected = "{\"id\":1,\"name\":\"feature\",\"dataType\":\"string\",\"categories\":[{\"id\":2,\"code\":\"code1\",\"label\":\"label1\",\"description\":\"description1\"},{\"id\":2,\"code\":\"code2\",\"label\":\"label2\",\"description\":\"description2\"}]}";
			assertEquals(output, expected);
		}
		finally
		{
			bos.close();
		}
	}

	@Test
	public void handleRequest_getFeature_noCategory() throws Exception
	{
		// mock db
		Database db = mock(Database.class);

		ObservableFeature feature = new ObservableFeature();
		feature.setId(1);
		feature.setName("feature");

		when(db.find(ObservableFeature.class, new QueryRule(ObservableFeature.ID, Operator.EQUALS, 1))).thenReturn(
				Collections.singletonList(feature));

		ProtocolViewerController controller = new ProtocolViewerController("test", mock(ScreenController.class));

		// mock request
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest("GET", null);
		httpServletRequest.setParameter("featureid", "1");
		httpServletRequest.setParameter("__action", "download_json_getfeature");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			controller.handleRequest(db, new MolgenisRequest(httpServletRequest), bos);
			String output = new String(bos.toByteArray(), Charset.forName("UTF-8"));

			String expected = "{\"id\":1,\"name\":\"feature\",\"dataType\":\"string\"}";
			assertEquals(output, expected);
		}
		finally
		{
			bos.close();
		}
	}

	@Test
	public void handleRequest_download_emeasure() throws Exception
	{
		// mock db
		Database db = mock(Database.class);

		ObservableFeature feature1 = new ObservableFeature();
		feature1.setId(1);
		feature1.setName("feature1");
		feature1.setIdentifier("featureid1");

		ObservableFeature feature2 = new ObservableFeature();
		feature2.setId(2);
		feature2.setName("feature2");
		feature2.setIdentifier("featureid2");

		ObservableFeature feature3 = new ObservableFeature();
		feature3.setId(3);
		feature3.setName("feature3");
		feature3.setIdentifier("featureid3");

		when(db.find(ObservableFeature.class, new QueryRule(ObservableFeature.ID, Operator.IN, Arrays.asList(1, 2, 3))))
				.thenReturn(Arrays.asList(feature1, feature2, feature3));

		ProtocolViewerController controller = new ProtocolViewerController("test", mock(ScreenController.class));

		// mock request
		MockHttpServletRequest httpRequest = new MockHttpServletRequest();
		httpRequest.setMethod("GET");
		MolgenisRequest request = mock(MolgenisRequest.class);
		when(request.getRequest()).thenReturn(httpRequest);
		when(request.getString("features")).thenReturn("1,2,3");
		when(request.getAction()).thenReturn("download_emeasure");
		MockHttpServletResponse httpResponse = new MockHttpServletResponse();
		when(request.getResponse()).thenReturn(httpResponse);

		controller.handleRequest(db, request);

		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<QualityMeasureDocument classCode=\"CONTAINER\" moodCode=\"DEF\"\n"
				+ "  xmlns=\"urn:hl7-org:v3\"\n"
				+ "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "  xsi:schemaLocation=\"urn:hl7-org:v3 multicacheschemas/REPC_MT000100UV01.xsd\" xsi:type=\"REPC_MT000100UV01.Organizer\">\n"
				+ "  <subjectOf>\n" + "    <measureAttribute>\n"
				+ "      <code code=\"feature1\" codeSystem=\"TBD\" displayName=\"null\"/>\n"
				+ "      <value code=\"dunno\" codeSystem=\"TBD\"\n"
				+ "        displayName=\"This should be the mappingsname\" xsi:type=\"string\"/>\n"
				+ "    </measureAttribute>\n" + "  </subjectOf>\n" + "  <subjectOf>\n" + "    <measureAttribute>\n"
				+ "      <code code=\"feature2\" codeSystem=\"TBD\" displayName=\"null\"/>\n"
				+ "      <value code=\"dunno\" codeSystem=\"TBD\"\n"
				+ "        displayName=\"This should be the mappingsname\" xsi:type=\"string\"/>\n"
				+ "    </measureAttribute>\n" + "  </subjectOf>\n" + "  <subjectOf>\n" + "    <measureAttribute>\n"
				+ "      <code code=\"feature3\" codeSystem=\"TBD\" displayName=\"null\"/>\n"
				+ "      <value code=\"dunno\" codeSystem=\"TBD\"\n"
				+ "        displayName=\"This should be the mappingsname\" xsi:type=\"string\"/>\n"
				+ "    </measureAttribute>\n" + "  </subjectOf>\n" + "</QualityMeasureDocument>\n";
		assertEquals(httpResponse.getContentAsString(), expected);
	}

	@Test
	public void handleRequest_download_emeasure_noFeatures() throws Exception
	{
		// mock db
		Database db = mock(Database.class);

		ProtocolViewerController controller = new ProtocolViewerController("test", mock(ScreenController.class));

		// mock request
		MockHttpServletRequest httpRequest = new MockHttpServletRequest();
		httpRequest.setMethod("GET");
		MolgenisRequest request = mock(MolgenisRequest.class);
		when(request.getRequest()).thenReturn(httpRequest);
		when(request.getString("features")).thenReturn(null);
		when(request.getAction()).thenReturn("download_emeasure");
		MockHttpServletResponse httpResponse = new MockHttpServletResponse();
		when(request.getResponse()).thenReturn(httpResponse);

		controller.handleRequest(db, request);

		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<QualityMeasureDocument classCode=\"CONTAINER\" moodCode=\"DEF\"\n"
				+ "  xmlns=\"urn:hl7-org:v3\"\n"
				+ "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "  xsi:schemaLocation=\"urn:hl7-org:v3 multicacheschemas/REPC_MT000100UV01.xsd\" xsi:type=\"REPC_MT000100UV01.Organizer\"/>\n";
		assertEquals(httpResponse.getContentAsString(), expected);
	}

	@Test
	public void handleRequest_download_xls_protocol() throws Exception
	{
		// mock db
		Database db = mock(Database.class);

		DataSet dataSet = new DataSet();
		dataSet.setId(1);
		dataSet.setName("dataset");
		dataSet.setProtocolUsed_Identifier("protocol1");

		ObservableFeature feature1 = new ObservableFeature();
		feature1.setId(1);
		feature1.setName("feature1");
		feature1.setIdentifier("featureid1");
		feature1.setDescription("this is feature1");

		ObservableFeature feature2 = new ObservableFeature();
		feature2.setId(2);
		feature2.setName("feature2");
		feature2.setIdentifier("featureid2");
		feature2.setDescription("this is feature2");

		ObservableFeature feature3 = new ObservableFeature();
		feature3.setId(3);
		feature3.setName("feature3");
		feature3.setIdentifier("featureid3");
		feature3.setDescription("this is feature3");

		when(db.find(DataSet.class, new QueryRule(DataSet.ID, Operator.EQUALS, 1))).thenReturn(
				Collections.singletonList(dataSet));
		when(db.find(ObservableFeature.class, new QueryRule(ObservableFeature.ID, Operator.IN, Arrays.asList(1, 2, 3))))
				.thenReturn(Arrays.asList(feature1, feature2, feature3));

		ProtocolViewerController controller = new ProtocolViewerController("test", mock(ScreenController.class));

		// mock request
		MockHttpServletRequest httpRequest = new MockHttpServletRequest();
		httpRequest.setMethod("GET");
		MolgenisRequest request = mock(MolgenisRequest.class);
		when(request.getRequest()).thenReturn(httpRequest);
		when(request.getInt("datasetid")).thenReturn(1);
		when(request.getString("features")).thenReturn("1,2,3");
		when(request.getAction()).thenReturn("download_xls");
		MockHttpServletResponse httpResponse = new MockHttpServletResponse();
		when(request.getResponse()).thenReturn(httpResponse);

		controller.handleRequest(db, request);

		assertEquals(DigestUtils.md5Hex(httpResponse.getContentAsByteArray()), "4351a541e9cc6f62bd76d05df5a2d865");
	}

	@Test
	public void handleRequest_download_xls_noProtocol() throws Exception
	{
		// mock db
		Database db = mock(Database.class);

		DataSet dataSet = new DataSet();
		dataSet.setId(1);
		dataSet.setName("dataset");

		ObservableFeature feature1 = new ObservableFeature();
		feature1.setId(1);
		feature1.setName("feature1");
		feature1.setIdentifier("featureid1");
		feature1.setDescription("this is feature1");

		ObservableFeature feature2 = new ObservableFeature();
		feature2.setId(2);
		feature2.setName("feature2");
		feature2.setIdentifier("featureid2");
		feature2.setDescription("this is feature2");

		ObservableFeature feature3 = new ObservableFeature();
		feature3.setId(3);
		feature3.setName("feature3");
		feature3.setIdentifier("featureid3");
		feature3.setDescription("this is feature3");

		when(db.find(DataSet.class, new QueryRule(DataSet.ID, Operator.EQUALS, 1))).thenReturn(
				Collections.singletonList(dataSet));
		when(db.find(ObservableFeature.class, new QueryRule(ObservableFeature.ID, Operator.IN, Arrays.asList(1, 2, 3))))
				.thenReturn(Arrays.asList(feature1, feature2, feature3));

		ProtocolViewerController controller = new ProtocolViewerController("test", mock(ScreenController.class));

		// mock request
		MockHttpServletRequest httpRequest = new MockHttpServletRequest();
		httpRequest.setMethod("GET");
		MolgenisRequest request = mock(MolgenisRequest.class);
		when(request.getRequest()).thenReturn(httpRequest);
		when(request.getInt("datasetid")).thenReturn(1);
		when(request.getString("features")).thenReturn("1,2,3");
		when(request.getAction()).thenReturn("download_xls");
		MockHttpServletResponse httpResponse = new MockHttpServletResponse();
		when(request.getResponse()).thenReturn(httpResponse);

		controller.handleRequest(db, request);

		assertEquals(DigestUtils.md5Hex(httpResponse.getContentAsByteArray()), "c8953444319fe6add92c0aa8637ed2df");
	}

	@Test
	public void handleRequest_download_xls_noFeatures() throws Exception
	{
		// mock db
		Database db = mock(Database.class);

		DataSet dataSet = new DataSet();
		dataSet.setId(1);
		dataSet.setName("dataset");

		when(db.find(DataSet.class, new QueryRule(DataSet.ID, Operator.EQUALS, 1))).thenReturn(
				Collections.singletonList(dataSet));

		ProtocolViewerController controller = new ProtocolViewerController("test", mock(ScreenController.class));

		// mock request
		MockHttpServletRequest httpRequest = new MockHttpServletRequest();
		httpRequest.setMethod("GET");
		MolgenisRequest request = mock(MolgenisRequest.class);
		when(request.getRequest()).thenReturn(httpRequest);
		when(request.getInt("datasetid")).thenReturn(1);
		when(request.getString("features")).thenReturn("1,2,3");
		when(request.getAction()).thenReturn("download_xls");
		MockHttpServletResponse httpResponse = new MockHttpServletResponse();
		when(request.getResponse()).thenReturn(httpResponse);

		controller.handleRequest(db, request);

		assertEquals(DigestUtils.md5Hex(httpResponse.getContentAsByteArray()), "0e4cf5b90004d86d2967e189f8094658");
	}
}
