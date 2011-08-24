<#macro plugins_breedingplugin_ManageLitters screen>
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

<#if screen.action == "ShowLitters">

	<p><a href="molgenis.do?__target=${screen.name}&__action=AddLitter">Make new litter</a></p>
	
	<#if screen.litterList?exists>
		<#if screen.litterList?size gt 0>
			<h2>Unweaned litters</h2>
			<table cellpadding="10" cellspacing="2" border="1">
				<tr>
					<th>Name</th>
					<th>Parentgroup</th>
					<th>Birth date</th>
					<th>Size</th>
					<th>Size approximate?</th>
					<th></th>
				</tr>
			<#list screen.litterList as litter>
				<tr>
					<td style='padding:5px'>${litter.name}</td>
					<td style='padding:5px'>${litter.parentgroup}</td>
					<td style='padding:5px'>${litter.birthDate}</td>
					<td style='padding:5px'>${litter.size}</td>
					<td style='padding:5px'>${litter.isSizeApproximate}</td>
					<td style='padding:5px'><a href="molgenis.do?__target=${screen.name}&__action=ShowWean&id=${litter.id?string.computer}">Wean</a></td>
				</tr>
			</#list>
			</table>
		</#if>
	</#if>
	
	<#if screen.genoLitterList?exists>
		<#if screen.genoLitterList?size gt 0>
			<h2>Weaned litters that have not been genotyped yet</h2>
			<table cellpadding="10" cellspacing="2" border="1">
				<tr>
					<th>Name</th>
					<th>Parentgroup</th>
					<th>Birth date</th>
					<th>Wean date</th>
					<th>Size at birth</th>
					<th>Size at weaning</th>
					<th></th>
					<th></th>
				</tr>
			<#list screen.genoLitterList as litter>
				<tr>
					<td style='padding:5px'>${litter.name}</td>
					<td style='padding:5px'>${litter.parentgroup}</td>
					<td style='padding:5px'>${litter.birthDate}</td>
					<td style='padding:5px'>${litter.weanDate}</td>
					<td style='padding:5px'>${litter.size}</td>
					<td style='padding:5px'>${litter.weanSize}</td>
					<td style='padding:5px'><a href="molgenis.do?__target=${screen.name}&__action=MakeTmpLabels&id=${litter.id?string.computer}">Make temporary cage labels</a></td>
					<td style='padding:5px'><a href="molgenis.do?__target=${screen.name}&__action=ShowGenotype&id=${litter.id?string.computer}">Genotype</a></td>
				</tr>
			</#list>
			</table>
		</#if>
	</#if>
	
	<#if screen.doneLitterList?exists>
		<#if screen.doneLitterList?size gt 0>
			<h2>Weaned and genotyped litters</h2>
			<table cellpadding="10" cellspacing="2" border="1">
				<tr>
					<th>Name</th>
					<th>Parentgroup</th>
					<th>Birth date</th>
					<th>Wean date</th>
					<th>Size at birth</th>
					<th>Size at weaning</th>
					<th></th>
				</tr>
			<#list screen.doneLitterList as litter>
				<tr>
					<td style='padding:5px'>${litter.name}</td>
					<td style='padding:5px'>${litter.parentgroup}</td>
					<td style='padding:5px'>${litter.birthDate}</td>
					<td style='padding:5px'>${litter.weanDate}</td>
					<td style='padding:5px'>${litter.size}</td>
					<td style='padding:5px'>${litter.weanSize}</td>
					<td style='padding:5px'><a href="molgenis.do?__target=${screen.name}&__action=MakeDefLabels&id=${litter.id?string.computer}">Make definitive cage labels</a></td>
				</tr>
			</#list>
			</table>
		</#if>
	</#if>
	
<#elseif screen.action == "MakeTmpLabels">

	<#if screen.labelDownloadLink??>
		<p>${screen.labelDownloadLink}</p>
	</#if>
	
	<p><a href="molgenis.do?__target=${screen.name}&__action=ShowLitters">Back to overview</a></p>
	
<#elseif screen.action == "MakeDefLabels">

	<#if screen.labelDownloadLink??>
		<p>${screen.labelDownloadLink}</p>
	</#if>
	
	<p><a href="molgenis.do?__target=${screen.name}&__action=ShowLitters">Back to overview</a></p>

<#elseif screen.action == "AddLitter">

	<p><a href="molgenis.do?__target=${screen.name}&__action=ShowLitters">Back to overview</a></p>

	<div id='newlitter_name_part' class='row' style="width:700px">
		<label for='littername'>Litter name:</label>
		<input type='text' class='textbox' name='littername' id='littername' value='<#if screen.litterName?exists>${screen.getLitterName()}</#if>' />
	</div>
	
	<!-- Parent group -->
	<div id="parentgroupselect" class="row" style='clear:left'>
		<label for="parentgroup">Parent group:</label>
		<select id='parentgroup' name='parentgroup'>
		<#if screen.parentgroupList?exists>
			<#list screen.parentgroupList as pgl>
				<option value='${pgl.id?string.computer}'>${pgl.name}</option>
			</#list>
		</#if>
		</select>
	</div>
	
	<!-- Date of birth -->
	<div id='newlitter_datevalue_part' class='row'>
		<label for='birthdate'>Birth date:</label>
		<input type='text' class='textbox' id='birthdate' name='birthdate' value='<#if screen.birthdate?exists>${screen.getBirthdate()}</#if>' onclick='showDateInput(this)' autocomplete='off' />
	</div>
	
	<!-- Size -->
	<div id='newlitter_size_part' class='row'>
		<label for='littersize'>Litter size:</label>
		<input type='text' class='textbox' name='littersize' id='littersize' value='<#if screen.litterSize?exists>${screen.getLitterSize()}</#if>' />
	</div>
	
	<!-- Size approximate? -->
	<div id="sizeapp_div" class="row">
		<label for="sizeapp_toggle">Size approximate:</label>
		<input type="checkbox" id="sizeapp_toggle" name="sizeapp_toggle" value="sizeapp" checked="yes" />
	</div>
	
	<!-- Add button -->
	<div id='newlitter_buttons_part' class='row'>
		<input type='submit' id='addlitter' class='addbutton' value='Add' onclick="__action.value='ApplyAddLitter'" />
	</div>
	
<#elseif screen.action == "ShowWean">

	<p><a href="molgenis.do?__target=${screen.name}&__action=ShowLitters">Back to overview</a></p>
	<!-- Date and time of weaning -->
	<div id='weandatediv' class='row'>
		<label for='weandate'>Wean date:</label>
		<input type='text' class='textbox' id='weandate' name='weandate' value='<#if screen.weandate?exists>${screen.getWeandate()}</#if>' onclick='showDateInput(this)' autocomplete='off' />
	</div>
	<!-- Size -->
	<div id='weansize_part1' class='row'>
		<label for='weansizefemale'>Nr. of females:</label>
		<input type='text' class='textbox' name='weansizefemale' id='weansizefemale' value='<#if screen.weanSizeFemale?exists>${screen.getWeanSizeFemale()}</#if>' />
	</div>
	<div id='weansize_part2' class='row'>
		<label for='weansizemale'>Nr. of males:</label>
		<input type='text' class='textbox' name='weansizemale' id='weansizemale' value='<#if screen.weanSizeMale?exists>${screen.getWeanSizeMale()}</#if>' />
	</div>
	<div id="divnamebase" class="row">
		<label for="namebase">Name base (may be empty):</label>
		<select id="namebase" name="namebase" onchange="updateStartNumber(this.value)">
			<option value=""></option>
			<option value="New">New (specify below)</option>
			<#list screen.bases as base>
				<option value="${base}">${base}</option>
			</#list>
		</select>
	</div>
	<input id="startnumberhelper" type="hidden" value="${screen.getStartNumberHelperContent()}">
	<div id="divnewnamebase" class="row">
		<label for="newnamebase">New name base:</label>
		<input type="text" name="newnamebase" id="newnamebase" class="textbox" />
	</div>
	<div id="divstartnumber" class="row">
		<label for="startnumber">Name numbering starts at:</label>
		<input type="text" name="startnumber" id="startnumber" class="textbox" value="${screen.getStartNumberForEmptyBase()?string.computer}" />
	</div>
	<!-- Add button -->
	<div id='addlitter' class='row'>
		<input type='submit' id='wean' class='addbutton' value='Wean' onclick="__action.value='Wean'" />
	</div>

<#elseif screen.action == "ShowGenotype">
	
	<p><a href="molgenis.do?__target=${screen.name}&__action=ShowLitters">Back to overview</a></p>
	
	<h2>Genotype litter</h2>
	
	<p>${screen.parentInfo}</p>
	
	<table>
		<tr>
			<th><strong>Animal name</strong></th>
			<th><strong>Birth date</strong></th>
			<th><strong>Sex</strong></th>
			<th><strong>Color</strong></th>
			<th><strong>Earmark</strong></th>
			<th><strong>Background</strong></th>
			<th><strong>Gene name</strong></th>
			<th><strong>Gene state</strong></th>
		</tr>
	<#assign animalCount = 0>
	<#list screen.getAnimalsInLitter() as animal>
		<#assign animalId = animal.id>
		<tr>
			<td>${animal.name}</td>
			<td>
				<input type='text' id='dob_${animalCount}' name='dob_${animalCount}' value='${screen.getAnimalBirthDate(animalId)}' onclick='showDateInput(this)' autocomplete='off' />
			</td>
			<td>
				<select id='sex_${animalCount}' name='sex_${animalCount}'>
				<#if screen.sexList?exists>
					<#list screen.sexList as sex>
						<option value='${sex.id?string.computer}'
						<#if screen.getAnimalSex(animalId) = sex.id>
							selected="selected"
						</#if>
						>${sex.name}</option>
					</#list>
				</#if>
				</select>
			</td>
			<td>
				<select id='color_${animalCount}' name='color_${animalCount}'>
				<#if screen.colorList?exists>
					<#list screen.colorList as color>
						<option value='${color}'
						<#if screen.getAnimalColor(animalId) = color>
							selected="selected"
						</#if>
						>${color}</option>
					</#list>
				</#if>
				</select>
			</td>
			<td>
				<select id='earmark_${animalCount}' name='earmark_${animalCount}'>
				<#if screen.earmarkList?exists>
					<#list screen.earmarkList as earmark>
						<option value='${earmark.code_String}'
						<#if screen.getAnimalEarmark(animalId) = earmark.code_String>
							selected="selected"
						</#if>
						>${earmark.code_String}</option>
					</#list>
				</#if>
				</select>
			</td>
			<td>
				<select id='background_${animalCount}' name='background_${animalCount}'>
				<#if screen.backgroundList?exists>
					<#list screen.backgroundList as background>
						<option value='${background.id?string.computer}'
						<#if screen.getAnimalBackground(animalId) = background.id>
							selected="selected"
						</#if>
						>${background.name}</option>
					</#list>
				</#if>
				</select>
			</td>
			<td>
				<select id='geneName_${animalCount}' name='geneName_${animalCount}'>
				<#if screen.geneNameList?exists>
					<#list screen.geneNameList as geneName>
						<option value='${geneName}'
						<#if screen.getAnimalGeneName(animalId) = geneName>
							selected="selected"
						</#if>
						>${geneName}</option>
					</#list>
				</#if>
				</select>
			</td>
			<td>
				<select id='geneState_${animalCount}' name='geneState_${animalCount}'>
				<#if screen.geneStateList?exists>
					<#list screen.geneStateList as geneState>
						<option value='${geneState}'
						<#if screen.getAnimalGeneState(animalId) = geneState>
							selected="selected"
						</#if>
						>${geneState}</option>
					</#list>
				</#if>
				</select>
			</td>
		</tr>
		<#assign animalCount = animalCount + 1>
	</#list>
	</table>
	<!-- "+" button for extra geneName + geneState columns, TODO get working
	<input type='submit' class='addbutton' value='+' onclick="" /> -->
	
	<!-- Save button -->
	<div class='row'>
		<input type='submit' id='save' class='addbutton' value='Save' onclick="__action.value='Genotype'" />
	</div>
	
</#if>
	
<#--end of your plugin-->	
			</div>
		</div>
	</div>
</form>
</#macro>
