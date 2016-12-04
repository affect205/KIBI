window.org_alexside_vaadin_desktop_qa_TagCloud =
    function() {
        // Create the component
        var tagcloud =
            new kibi.TagCloud(this.getElement());

        // Handle changes from the server-side
        this.onStateChange = function() {
            tagcloud.setValue(this.getState().value);
            tagcloud.setTags(this.getState().tags);
        };

        // Pass user interaction to the server-side
        var self = this;
        tagcloud.click = function() {
            self.onClick(tagcloud.getValue());
        };
        tagcloud.onTagClick = function (tagId) {
            self.onTagClick(tagId);
        };
    };