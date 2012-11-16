function selectDataSet(id) {
	// get
	getDataSet(id, function(data) {
		// update
		setFeatureDetails(null);
		updateFeatureSelection(null);
		updateProtocolView(data.protocol);
		// set selected data set
		$(document).data('datasets').selected = id;
	});
}

function getDataSet(id, callback) {
	var dataSets = $(document).data('datasets');
	if(typeof dataSets === 'undefined') {
		dataSets = {};
		$(document).data('datasets', dataSets);
	}
	
	if(!dataSets[id]) {
		$.getJSON('molgenis.do?__target=ProtocolViewer&__action=download_json_getdataset&datasetid=' + id, function(data) {
			dataSets[id] = data;
			callback(data);
		});
	} else {
		callback(dataSets[id]);
	}
}

function getFeature(id, callback) {
	$.getJSON('molgenis.do?__target=ProtocolViewer&__action=download_json_getfeature&featureid=' + id, function(data) {
		callback(data);
	});
}

function updateProtocolView(protocol) {
	var container = $('#dataset-browser');
	if(container.children('ul').length > 0) {
		container.dynatree('destroy');
	}
	
	container.empty();
	if(typeof protocol === 'undefined') {
		container.append("<p>Catalog does not describe variables</p>");
		return;
	}
	
	// recursively build tree for protocol	
	function buildTree(protocol) {
		// add protocol
		var item = $('<li class="folder"/>').attr('data', 'key: "' + protocol.id + '", title:"' + protocol.name + '"');
		// add protocol: features
		var list = $('<ul />');
		if(protocol.features) {
			$.each(protocol.features, function(i, feature) {
				var item = $('<li />').attr('data', 'key: "' + feature.id + '", title:"' + feature.name + '"');
				item.appendTo(list);
			});
		}
		// add protocol: subprotocols
		if(protocol.subProtocols) {
			$.each(protocol.subProtocols, function(i, subProtocol) {
				list.append(buildTree(subProtocol));
			});
		}
		list.appendTo(item);
		
		return item;
	};
	
	// append tree to DOM
	var tree = $('<ul />').append(buildTree(protocol));
	container.append(tree);

	// render tree and open first branch
	container.dynatree({
		checkbox: true,
		selectMode: 3,
		minExpandLevel: 2,
		debugLevel: 0,
		onClick: function(node, event) {
			if(node.getEventTargetType(event) == "title" && !node.data.isFolder)
				getFeature(node.data.key, function(data) { setFeatureDetails(data); });
		},
		onSelect: function(select, node) {
			// update feature details
			if(select && !node.data.isFolder)
				getFeature(node.data.key, function(data) { setFeatureDetails(data); });
			else
				setFeatureDetails(null);

			// update feature selection
			updateFeatureSelection(node.tree);
		}	
	});
}

function setFeatureDetails(feature) {
	var container = $('#feature-details').empty(); 
	if(feature == null) {
		container.append("<p>Select a variable to display variable details</p>");
		return;
	}
	
	var table = $('<table />');
	table.append('<tr><td>' + "Name:" + '</td><td>' + feature.name + '</td></tr>');
	table.append('<tr><td>' + "Description:" + '</td><td>' + feature.description + '</td></tr>');
	table.append('<tr><td>' + "Data type:" + '</td><td>' + feature.dataType + '</td></tr>');
	
	table.addClass('listtable feature-table');
	table.find('td:first-child').addClass('feature-table-col1');
	container.append(table);
	
	if(feature.categories) {
		var categoryTable = $('<table class="table table-striped table-condensed" />');
		$('<thead />').append('<th>Code</th><th>Label</th><th>Description</th>').appendTo(categoryTable);
		$.each(feature.categories, function(i, category){
			var row = $('<tr />');
			$('<td />').text(category.code).appendTo(row);
			$('<td />').text(category.name).appendTo(row);
			$('<td />').text(category.description).appendTo(row);
			row.appendTo(categoryTable);		
		});
		
		container.append(categoryTable);
	}
}

function updateFeatureSelection(tree) {
	var container = $('#feature-selection').empty();
	if(tree == null) {
		container.append("<p>No variables selected</p>");
		return;
	}
	
	var nodes = tree.getSelectedNodes();
	if(nodes == null || nodes.length == 0) {
		container.append("<p>No variables selected</p>");
		return;
	}
	
	var table = $('<table class="table table-striped table-condensed table-hover" />');
	$('<thead />').append('<th>Variables</th><th>Protocol</th><th>Delete</th>').appendTo(table);
	$.each(nodes, function(i, node) {
		if(!node.data.isFolder) {
			var name = node.data.title;
			var protocol_name = node.parent.data.title;
			
			var row = $('<tr />');
			$('<td />').text(name !== undefined ? name : "").appendTo(row);
			$('<td />').text(protocol_name != undefined ? protocol_name : "").appendTo(row);
			
			var deleteButton = $('<input type="image" src="generated-res/img/cancel.png" alt="delete">');
			deleteButton.click($.proxy(function() {
				tree.getNodeByKey(node.data.key).select(false);
				return false;
			}, this));
			$('<td />').append(deleteButton).appendTo(row);
			
			row.appendTo(table);
		}
	});
	table.addClass('listtable selection-table');
	container.append(table);
}

function getSelectedFeaturesURL(format) {
 	var tree = $('#dataset-browser').dynatree("getTree");;
	var features = $.map(tree.getSelectedNodes(), function(node){
        return node.data.isFolder ? null : node.data.key;
    });
	
	var id = $(document).data('datasets').selected;
	return 'molgenis.do?__target=ProtocolViewer&__action=download_' + format + '&datasetid=' + id + '&features=' + features.join();
}

function processSearch(query) {
	if(query) {
		var dataSet = $(document).data('datasets').selected;
		if(dataSet && dataSet.protocol) {
			searchProtocol(dataSet.protocol, new RegExp(query, 'i'));
		}
	}
}

function searchProtocol(protocol, regexp) {
	if(matchProtocol(protocol, regexp))
		console.log("found protocol: " + protocol.name);
	
	if(protocol.features) {
		$.each(protocol.features, function(i, feature) {
			if(matchFeature(feature, regexp))
				console.log("found feature: " + feature.name);
		});
	}
	
	if(protocol.subProtocols) {
		$.each(protocol.subProtocols, function(i, subProtocol) {
			searchProtocol(subProtocol, regexp);
		});
	}
}

function matchProtocol(protocol, regexp) {
	return protocol.name && protocol.name.search(regexp) != -1;
}

function matchFeature(feature, regexp) {
	if(feature.name && feature.name.search(regexp) != -1)
		return true;
	if(feature.description && feature.description.search(regexp) != -1)
		return true;
	
	if(feature.categories) {
		$.each(feature.categories, function(i, category) {
			if(matchCategory(category, regexp))
				console.log("found category: " + category);
		});
	}
}

function matchCategory(category, regexp) {
	if(category.description && category.description.search(regexp) != -1)
		return true;
	if(category.label && category.label.search(regexp) != -1)
		return true;
}