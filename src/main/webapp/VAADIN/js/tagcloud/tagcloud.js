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
        "ul.tx3-tag-cloud li a {" +
        "display: block;" +
        "text-decoration: none;" +
        "color: #c9c9c9;" +
        "padding: 3px 10px;" +
        "}" +
        "ul.tx3-tag-cloud li a:hover {" +
        "color: #000000;" +
        "-webkit-transition: color 250ms linear;" +
        "-moz-transition: color 250ms linear;" +
        "-o-transition: color 250ms linear;" +
        "-ms-transition: color 250ms linear;" +
        "transition: color 250ms linear;" +
        "}" ;

    element.innerHTML =
        "<style> " + style + " </style>" +
        "<div class='textinput'>Enter a value: " +
        "<input type='text' name='value'/>" +
        "<input type='button' value='Click'/>" +
        "</div>" +
        "<ul id='tagcloud'>" +
        "<li data-weight='50' class='tag-item' object-id='1'><a href='#'>HTML5</a></li>" +
        "<li data-weight='32' class='tag-item' object-id='2'><a href='#'>NoSQL</a></li>" +
        "<li data-weight='24' class='tag-item' object-id='3'><a href='#'>CSS3</a></li>" +
        "<li data-weight='18' class='tag-item' object-id='4'><a href='#'>Spring</a></li>" +
        "<li data-weight='28' class='tag-item' object-id='5'><a href='#'>Javascript</a></li>" +
        "<li data-weight='30' class='tag-item' object-id='6'><a href='#'>Hibernate</a></li>" +
        "<li data-weight='46' class='tag-item' object-id='7'><a href='#'>Java</a></li>" +
        "<li data-weight='28' class='tag-item' object-id='8'><a href='#'>Vaadin</a></li>" +
        "<li data-weight='32' class='tag-item' object-id='9'><a href='#'>jQuery</a></li>" +
        "</ul>";

    // $(document).ready(function(){
    //     $("#tagcloud").tx3TagCloud({
    //         multiplier: 5
    //     });
    // });
    console.log("init tag cloud...");

    $("#tagcloud").tx3TagCloud({multiplier: 4});

    $(".tag-item").click(function () {
        alert($(this).attr("object-id"));
    });

    // Style it
    // element.style.border = "thin solid #1b87e3";
    element.style.display = "inline-block";
    element.style.width = "98%";
    element.style.height = "98%";

    // Getter and setter for the value property
    this.getValue = function () {
        return element.
        getElementsByTagName("input")[0].value;
    };
    this.setValue = function (value) {
        element.getElementsByTagName("input")[0].value =
            value;
    };
    this.getTags = function () {

    };
    this.setTags = function (tags) {
        if (!tags) return;
        console.log("tags...");
        console.log(tags);
        tags.forEach(function(tag) {
            var value =
                "<li class='tag-item' " +
                "data-weight='" + tag.weight + "'" +
                "object-id='" + tag.id + "'>" +
                "<a href='#'>" + tag.name + "</a>" +
                "</li>";
            $("#tagcloud").append(value);
        });
        $("#tagcloud").tx3TagCloud({multiplier: 4});
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
};