<#macro plugins_help_BbmriHelpPlugin screen>
<!-- normally you make one big form for the whole plugin-->
<form method="post" enctype="multipart/form-data" name="${screen.name}" action="">
	<!--needed in every form: to redirect the request to the right screen-->
	<input type="hidden" name="__target" value="${screen.name}">
	<!--needed in every form: to define the action. This can be set by the submit button-->
	<input type="hidden" name="__action">
	
<!-- this shows a title and border -->
	<div class="formscreen">
		<div class="form_header" id="${screen.getName()}">
		${screen.label}
		</div>
		
		<#--optional: mechanism to show messages-->
		<#list screen.getMessages() as message>
			<#if message.success>
		<p class="successmessage">${message.text}</p>
			<#else>
		<p class="errormessage">${message.text}</p>
			</#if>
		</#list>
		
		<div class="screenbody">
			<div class="screenpadding">	
<#--begin your plugin-->	

<h1>Catalogue of Dutch Biobanks</h1>
<h2>User manual</h2>

<h3>Step 1: How to access the catalogue</h3>
<p>The biobank catalogue can be found at <a href="https://catalogue.bbmri.nl/biobanks/">https://catalogue.bbmri.nl/biobanks/</a>.</p>
<p>To log in, click Login in the left hand menu. Are you a registered user? Then type in your user name and password. If you are not yet a registered user, click Register.</p>
<p>After login, a welcome page is displayed, showing you a few of the options you have. Your main means of navigation are the left hand and top right menus.</p>

<p><strong>The menus</strong><br />
The left hand menu is your guide throughout the application. It contains the following items:
<ul>
	<li>Welcome</li>
	<li>My Account</li>
	<li>Biobank Overview</li>
	<li>Admin (for administrators)</li>
	<li>Help</li>
	<li>Contact</li>
</ul>
</p>

<p>The top right hand menu contains the following items:
<ul>
	<li>Welcome (your name) - your personal settings can be revised by clicking here (see below)</li>
	<li>Logout</li>
	<li>Help</li>
	<li>Contact</li>
</ul>
</p>

<p><strong>Settings</strong><br />
The first item on the top right hand menu is 'Welcome (your name)' that can also be found in the left menu 'My account'. Here, you can fill in your personal information and change your password. A small note: The value of the field "Position" has to be inserted in the "Coordinator Roles" first.</p>
<p>Next to the Welcome button is the Logout option.</p>
<p>Once you are in Biobank Overview, an additional top menu is visible at the top left corner of the table. More on that in the Biobank Overview section.</p>

<h3>Step 2: How to access the biobank data</h3>
<p>Click Biobank Overview in the left hand menu. The first page of the biobank catalogue will appear.<p>
<p>The table contains several columns:
<ul>
	<li>The database number and a link to edit this record which is hidden for simple users. </li>
	<li>canRead = who can read this entry (hidden for simple users, visible in "Admin" => "Admin Biobank")</li>
	<li>canWrite = who can write this entry (hidden for simple users, visible in "Admin" => "Admin Biobank")</li>
	<li>Owns = who can edit or remove this entry (hidden for simple users, visible in "Admin" => "Admin Biobank")</li>
	<li>Cohort = the name of the cohort</li>
	<li>Acronym (hidden for simple users, visible in "Admin" => "Admin Biobank")</li>
	<li>Category = there are two categories: A, core biobanks (DNA available), and B, supporting biobanks (no DNA available)</li>
	<li>Subcategory = the possible subcategories </li>
	<li>Topic = more specific denominator of research topic, e.g. breast cancer</li>
	<li>Coordinators = names of the associated co-ordinators (hidden for simple users)</li>
	<li>Institutes = associated institutes</li>
	<li>Current n= = number of participants</li>
	<li>Biodata</li>
	<li>GWA data n=</li>
	<li>GWA platform</li>
	<li>GWA comments</li>
	<li>General comments</li>
	<li>Publications = relevant publications, to be filled</li>
</ul>
</p>

<p>The table can be sorted by the following columns : 
<ul>
	<li>Cohort</li>
	<li>Category</li>
	<li>SubCategory</li>
	<li>Current n= </li>
	<li>GWA data n=</li>
	<li>GWA platform</li>
	<li>GWA comments</li>
	<li>General comments</li>
	<li>Publications</li>
 (in alphabetical order) by clicking on the column heading. Clicking twice will give you the same sorting, only in Z-A order.</p>

<h3>Step 3: Working with the data: download, upload, edit, remove, search</h3>
<p>In the top left corner you see a small menu: File, Edit and View</p>
<p>File: here is where you can download or upload (if you are a privileged user) one or several records.</p>
<p>To download, choose if you want to download all visible records, only the ones you've selected, or all records. Selecting records is done by ticking the box between the 'Id' and the 'Cohort' (simple users) or 'Owns' column(for admin users).</p>
<p>To upload, choose Add in batch / upload CSV. A form will appear; fill out all the relevant fields, add the csv-data (by simply copying and pasting) and press the Save-icon. Your data will be sent to the BBMRI-NL office, where it will be checked for accuracy and redundancy before publication.</p>
<p>You can add a simple record by filling  out all the relevant fields in "Add in batch / upload CSV", or add a file of records from "File=>Upload CSV File" by browsing through the pop up window that come by clicking "Choose file".</p> 
<p><em>What is a csv file? Csv stands for 'comma-separated values', that is, data you would expect to find in a table, complete with columns, rows and column headers, is now grouped together with only commas between each data entry. Most spreadsheet formats, e.g. Excel files can be easily converted to (saved as) csv files.</em></p>
<p>Edit: here, you can add, update or remove a selected record - pending approval by the BBMRI-NL office. <span style="color:red">NB: Update selected record is not a regular update. This feature allows you to update ALL the fields of the record. The regular update where the current values of a record are visible is the button between the 'id' and the 'Cohort' on every record. </span></p>
<p>View: select the view you're most comfortable with. You can choose to view 5, 10, 20, 50, 100 or 500 records per page.</p>
<p><strong>Search:</strong> you can search the records in several ways. As soon as you click the form field next to 'Search', options appear:
<ol>
	<li>in which field(s) to search</li>
	<li>to search for a precise or a partial match</li>
</ol>
Enter your criteria and press Enter. A filter will be applied, showing only the entries that answer to your search criteria. To remove this filter once you're done, press the red cross that appears in the blue bar. To add search criteria, fill in new criteria while the first search selection is still displayed.</p>

<h3>Contact</h3>
<p>Should you encounter any difficulties that the Help file cannot solve, or have any comments regarding the application, do not hesitate to contact the BBMRI-NL office by (preferably) email or phone:<br />
<table>
	<tr>
		<td style='padding:5px'>Margreet Brandsma</td>
		<td style='padding:5px'>+31 71 5269412</td>
		<td style='padding:5px'><a href="mailto:m.brandsma@bbmri.nl">m.brandsma AT bbmri.nl</a></td>
	</tr>
	<tr>
		<td style='padding:5px'>Margot Heesakker-Heintz</td>
		<td style='padding:5px'>+31 71 5268498</td>
		<td style='padding:5px'><a href="mailto:m.heesakker@bbmri.nl">m.heesakker AT bbmri.nl</a></td>
	</tr>
</table>
</p>
	
<#--end of your plugin-->	
			</div>
		</div>
	</div>
</form>
</#macro>
