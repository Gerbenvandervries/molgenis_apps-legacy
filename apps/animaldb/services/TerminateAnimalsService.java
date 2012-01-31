package services;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.Query;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.server.MolgenisContext;
import org.molgenis.framework.server.MolgenisRequest;
import org.molgenis.framework.server.MolgenisResponse;
import org.molgenis.framework.server.MolgenisService;
import org.molgenis.pheno.Category;
import org.molgenis.pheno.Measurement;
import org.molgenis.pheno.ObservedValue;
import org.molgenis.util.HttpServletRequestTuple;
import org.molgenis.util.Tuple;

public class TerminateAnimalsService implements MolgenisService {
	private static final long serialVersionUID = -5860101269122494304L;
	private static Logger logger = Logger.getLogger(TerminateAnimalsService.class);
	
	private MolgenisContext mc;
	
	public TerminateAnimalsService(MolgenisContext mc)
	{
		this.mc = mc;
	}
	
	@Override
	public void handleRequest(MolgenisRequest request, MolgenisResponse response) throws ParseException,
			DatabaseException, IOException
	{
		
		PrintWriter out = response.getResponse().getWriter();
		try {
			Tuple req = request;

			Database db = request.getDatabase();
			//this.createLogin(db, request); NO LONGER NEEDED
			
			String tmpString = req.getString("animal").replace(".", "");
			tmpString = tmpString.replace(",", "");
			int animalId = Integer.parseInt(tmpString);
			if (animalId == 0) {
				out.print("");
			} else {
				java.sql.Date nowDb = new java.sql.Date(new Date().getTime());
				Query<ObservedValue> q = db.query(ObservedValue.class);
				q.addRules(new QueryRule(ObservedValue.DELETED, Operator.EQUALS, false));
				q.addRules(new QueryRule(ObservedValue.TARGET, Operator.EQUALS, animalId));
				q.addRules(new QueryRule(ObservedValue.FEATURE_NAME, Operator.EQUALS, "Experiment"));
				q.addRules(new QueryRule(ObservedValue.TIME, Operator.LESS_EQUAL, nowDb));
				q.addRules(new QueryRule(ObservedValue.ENDTIME, Operator.EQUALS, null));
				List<ObservedValue> valueList = q.find();
				if (valueList.size() == 1) {
					// Generate selectbox for Actual Discomfort
					Query<Category> codeQuery = db.query(Category.class);
					Measurement actualDiscomfort = db.query(Measurement.class).eq(Measurement.DELETED, false).
							eq(Measurement.NAME, "ActualDiscomfort").find().get(0);
					codeQuery.in(Category.ID, actualDiscomfort.getCategories_Id());
					List<Category> codeList = codeQuery.find();
					out.print("<div class='row'>");
					out.print("<label for='discomfort'>Actual discomfort:</label>");
					out.print("<select name='discomfort' id='discomfort'>");
					for (Category code : codeList) {
						out.print("<option value='" + code.getDescription() + "'>" + code.getCode_String() + 
								" (" + code.getDescription() + ")" + "</option>");
					}
					out.print("</select>");
					out.print("</div>");
						
					// Generate selectbox for ActualAnimalEndStatus
					codeQuery = db.query(Category.class);
					Measurement actualAnimalEndStatus = db.query(Measurement.class).eq(Measurement.DELETED, false)
							.eq(Measurement.NAME, "ActualAnimalEndStatus").find().get(0);
					codeQuery.in(Category.ID, actualAnimalEndStatus.getCategories_Id());
					codeList = codeQuery.find();
					out.print("<div class='row'>");
					out.print("<label for='endstatus'>Actual animal end status:</label>");
					out.print("<select name='endstatus' id='endstatus'>");
					for (Category code : codeList) {
						out.print("<option value='" + code.getDescription() + "'>" + code.getCode_String() + " (" + code.getDescription() + ")" + "</option>");
					}
					out.print("</select>");
					out.print("</div>");
						
				} else if (valueList.size() > 1) {
					out.print("<p>Animal in more than one experiment, something seems to be wrong.</p>");
				} // if size = 0, animal is not in experiment, so do not generate any fields
				
				// Always generate action button
				out.print("<div id='buttons_part' class='row'><input type='submit' class='addbutton' value='Apply' onclick=\"__action.value='applyDeath'\" /></div>");
			}
			
			out.flush();

			logger.info("serving " + request.getRequest().getRequestURI());
		} catch (Exception e) {
			e.printStackTrace(out);
		} finally {
			out.close();
		}
	}
}
