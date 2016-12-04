window.org_alexside_vaadin_desktop_qa_JsLabel = function() {
	var e = this.getElement();

	this.onStateChange = function() {
		e.innerHTML = this.getState().xhtml; 
	}
};