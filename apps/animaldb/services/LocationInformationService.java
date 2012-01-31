package services;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
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
import org.molgenis.pheno.Location;
import org.molgenis.pheno.ObservedValue;
import org.molgenis.protocol.ProtocolApplication;
import org.molgenis.util.HttpServletRequestTuple;
import org.molgenis.util.Tuple;

public class LocationInformationService implements MolgenisService {
	private static final long serialVersionUID = -5115596071747077428L;
	private static Logger logger = Logger.getLogger(LocationInformationService.class);
	
	private MolgenisContext mc;
	
	public LocationInformationService(MolgenisContext mc)
	{
		this.mc = mc;
	}
	
	@Override
	public void handleRequest(MolgenisRequest request, MolgenisResponse response) throws ParseException,
			DatabaseException, IOException
	{
		PrintWriter out = response.getResponse().getWriter();
		try
		{
			Tuple req = request;
			
			Database db = request.getDatabase();
		//	this.createLogin(db, request); NO LONGER NEEDED
			
			String tmpString = req.getString("loc").replace(".", "");
			tmpString = tmpString.replace(",", "");
			int locid = Integer.parseInt(tmpString);
			if (locid == 0) {
				out.print("");
			} else {
				Location currentLocation = db.find(Location.class, 
						new QueryRule(Location.ID, Operator.EQUALS, locid)).get(0);
				out.print(currentLocation.getName());
				Location superloc = new Location();
				boolean firstTime = true;
				while (true) {
					superloc = getSuperloc(db, locid);
					if (superloc != null){
						if (firstTime) {
							firstTime = false;
						} else {
							out.print(", which");
						}
						out.print(" is a sublocation of " + superloc.getName());
						locid = superloc.getId();
					} else {
						break;
					}
				}
			}
			out.flush();

			logger.info("serving " + request.getRequest().getRequestURI());
		}
		catch (Exception e)
		{
			e.printStackTrace(out);
		}
		finally
		{
			out.close();
		}
	}
	
	Location getSuperloc(Database db, int locid) throws DatabaseException, ParseException {
		
		Location emptyLocation = null;
		
		List<ProtocolApplication> eventList = db.find(ProtocolApplication.class, 
				new QueryRule(ProtocolApplication.PROTOCOL_NAME, Operator.EQUALS, "SetSublocationOf"));
		
		for (ProtocolApplication e : eventList) {
			Query<ObservedValue> q = db.query(ObservedValue.class);
			q.addRules(new QueryRule(ObservedValue.DELETED, Operator.EQUALS, false));
			q.addRules(new QueryRule(ObservedValue.PROTOCOLAPPLICATION, Operator.EQUALS, e.getId()));
			q.addRules(new QueryRule(ObservedValue.TARGET, Operator.EQUALS, locid));
			q.addRules(new QueryRule(Operator.SORTDESC, ObservedValue.TIME));
			// TODO: check for endtime?
			List<ObservedValue> valueList = new ArrayList<ObservedValue>();
			try {
				valueList = q.find();
			} catch (Exception ex) {
				ex.printStackTrace();
				return emptyLocation;
			}
			if (valueList.size() > 0) {
				ObservedValue currentValue = valueList.get(0);
				if (currentValue != null) {
					int superlocid = 0;
					if (currentValue.getRelation_Id() != null) {
						superlocid = currentValue.getRelation_Id();
					}
					if (superlocid > 0) {
						return db.find(Location.class, 
								new QueryRule(Location.ID, Operator.EQUALS, superlocid)).get(0);
					}
				}
			}
		}
		return emptyLocation;
	}
	
}
