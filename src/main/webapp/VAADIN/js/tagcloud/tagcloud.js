// Define the namespace
var kibi = kibi || {};

kibi.TagCloud = function (element) {
    var style =
        "ul.tx3-tag-cloud {} " +
        "ul.tx3-tag-cloud li {" +
        "display: block;" +
        "float: left;" +
        "list-style: none;" +
        "margin-right: 4px;" +
        "}" +
        "ul.tx3-tag-cloud li {" +
        "display: block;" +
        "text-decoration: none;" +
        "color: #c9c9c9;" +
        "padding: 3px 10px;" +
        "}" +
        "ul.tx3-tag-cloud li:hover {" +
        "color: #000000;" +
        "-webkit-transition: color 250ms linear;" +
        "-moz-transition: color 250ms linear;" +
        "-o-transition: color 250ms linear;" +
        "-ms-transition: color 250ms linear;" +
        "transition: color 250ms linear;" +
        "}" ;

    // 50, 32, 24

    element.innerHTML =
        "<style> " + style + " </style>" +
        "<div class='textinput'>Enter a value: " +
        "<input type='text' name='value'/>" +
        "<input type='button' value='Click'/>" +
        "</div>" +
        "<ul id='tagcloud'>" +
        // "<li class='tag-item' tag-weight='30' tag-id='1'>HTML5</li>" +
        // "<li class='tag-item' tag-weight='30' tag-id='2'>NoSQL</li>" +
        // "<li class='tag-item' tag-weight='30' tag-id='3'>CSS3</li>" +
        // "<li class='tag-item' tag-weight='18' tag-id='4'>Spring</li>" +
        // "<li class='tag-item' tag-weight='28' tag-id='5'>Javascript</li>" +
        // "<li class='tag-item' tag-weight='30' tag-id='6'>Hibernate</li>" +
        // "<li class='tag-item' tag-weight='46' tag-id='7'>Java</li>" +
        // "<li class='tag-item' tag-weight='28' tag-id='8'>Vaadin</li>" +
        // "<li class='tag-item' tag-weight='32' tag-id='9'>jQuery</li>"
        "</ul>";

    // Style it
    // element.style.border = "thin solid #1b87e3";
    element.style.display = "inline-block";
    element.style.width = "98%";
    element.style.height = "98%";

    // Getter and setter for the value property
    this.getValue = function () {
        return element.getElementsByTagName("input")[0].value;
    };
    this.setValue = function (value) {
        element.getElementsByTagName("input")[0].value = value;
    };
    this.getTags = function () {
        return [];
    };
    this.setTags = function (tags) {
        if (!tags) return;
        //$(".tag-item").remove();
        tags.forEach(function(tag) {
            var value =
                "<li class='tag-item' " +
                "tag-weight='" + tag.weight + "'" +
                "tag-id='" + tag.id + "'>" +
                tag.name +
                "</li>";
            $("#tagcloud").append(value);
        });
        $("#tagcloud").tx3TagCloud({multiplier: 2});
        $(".tag-item").click(function () {
            self.onTagClick($(this).attr("tag-id"));
        });
    };

    // Default implementation of the click handler
    this.click = function () {
        alert("Error: Must implement click() method");
    };

    // Set up button click
    var button = element.getElementsByTagName("input")[1];
    var self = this; // Can't use this inside the function
    button.onclick = function () {
        self.click();
    };

    // клик по тегу
    this.onTagClick = function (tagId) {
        alert("Error: Must implement onTagClick() method");
    };

    $(".tag-item").click(function () {
        self.onTagClick($(this).attr("tag-id"));
    });
};