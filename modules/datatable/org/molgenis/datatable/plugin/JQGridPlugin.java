package org.molgenis.datatable.plugin;



import java.sql.SQLException;

import org.molgenis.datatable.model.JdbcTable;
import org.molgenis.datatable.view.JQGridView;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.ui.GenericPlugin;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.framework.ui.ScreenView;
import org.molgenis.framework.ui.html.Container;
import org.molgenis.framework.ui.html.DivPanel;
import org.molgenis.util.Tuple;


/**
 * View data in a matrix.
 */
public class JQGridPlugin extends GenericPlugin
{
	private static final long serialVersionUID = 8804579908239186037L;
	private Container container = null;
	private DivPanel div = null;
	private JQGridView gridView;
	
	public JQGridPlugin(String name, ScreenController<?> parent)
	{
		super(name, parent);
	}

	@Override
	public void handleRequest(Database db, Tuple request)
	{
		System.out.println(this);
		final String action = request.getAction();
		System.out.println("test!");
		try {

			
		} catch(Exception e) {
			e.printStackTrace();
			this.getMessages().add(new ScreenMessage("Something went wrong while handling request: " + e.getMessage(), false));
		}
	}

	@Override
	public void reload(Database db)
	{
		
		try
		{
			gridView = new JQGridView("jqGridId", new JdbcTable("SELECT id, Father FROM Individual",db.getConnection()));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		container = new Container();
		div = new DivPanel();
		container.add(div);
		div.add(gridView);
    }
	
	public ScreenView getView()
    {
    	return container;
    }

	public String render() {
		return container.render();
	}
}
