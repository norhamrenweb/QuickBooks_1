/*
 The help2.js and tree2.css just apply to the single page layout
*/
checkForNewVersion(location.href);
function checkForNewVersion(href) {     
  if ($("#newver").length == 0 || href.indexOf("file:///") > -1 || href.indexOf("localhost/") > -1) return;
    try {
        var url = href.split('/');
        prod = url[url.length-3]; //get product
        v = prod.charAt(2);     //find version
        
        var vMap = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        var v2 = vMap.charAt(vMap.indexOf(v) + 1);
        var prodChar = prod.split("");
        prodChar[2] = v2;
        prod = prodChar.join("");
        
        url[url.length-3] = prod;
        //url[url.length-1] = "";
        
        href = url.join("/");

        $.ajax({
            url: href, success: function (data) {
                $("#newver").html("<i>A new version of this product is available! </i> [<a href='" + href + "'>learn more</a>]");
            }
        });
    }
    catch(err) { }
}

var treeWidthResizer = {
    isDrag: false,
    x:0,
    init:function(){
        $("#whleftcol").append("<div id='resizerCol' style='margin-left:280px;'></div>");
        /*
         *The event start have to be the virtual partition line.
         *But the event end just should be that mouse up in the table.
         * Maybe this just is a virtual "drag" event.
         */
        $("#resizerCol").mousedown(function () {
            treeWidthResizer.isDrag = true;
        });
        $("#whlayout").mouseup(function (e) {
            if (treeWidthResizer.isDrag) {
                $("#whleftcol").css("width", (e.pageX + 10) + "px");
                $("#resizerCol").css("marginLeft", (e.pageX-10) + "px");
            }
            treeWidthResizer.isDrag = false;
        });

        $("#whlayout tr:eq(1)").mousemove(function (e) {
          if (treeWidthResizer.isDrag) {
              $("#whsizer").css("width", e.pageX + "px");
          }
          if (treeWidthResizer.isDrag) {
              return false; //prevent to select other text content.
          }
        });

    }
}

treeWidthResizer.init();

$(document).ready(function () {
    //scrollto
    function scrollTo(id) {
        var link_id = id || location.hash;
        if (link_id) {
            $("#whtoc>ul").removeClass("nav");  //remove scrollspy 

            $("#whtoc li.active").removeClass("active");
            $("#whtoc li a[href='" + link_id + "']").parents("li").addClass("active");
            var topGap = 140;  //cdata
            topGap = $(link_id).is("h1,h2") ? 80 : topGap;  //nsoftware
            $('html, body').animate({ scrollTop: $(link_id).offset().top - topGap }, 500);
        
            //update page's title and desc
            if (link_id!="#default") {
                var chapterTitle = $(link_id).parents(".whiframe").prev("span.whtitle").attr("title");
                var chapterDesc = $(link_id).parents(".whiframe").prev("span.whtitle").attr("desc");
                if (chapterTitle) {
                    document.title = chapterTitle;
                }
                if (chapterDesc) {
                    $("meta[name='description']").attr('content', chapterDesc);
                }
            }

            //add scrollspy 
            setTimeout(function(){
                $("#whtoc>ul").addClass("nav"); setScrollspy();
            },600);
        
            /*fix the tree's width for IE*/
            var treeWidth = $("#resizerCol").css("margin-left");
            $("#resizerCol").css("margin-left","0px;");
            $("#resizerCol").css("margin-left",treeWidth);
        }
    };
    scrollTo();

    $("#whtoc li a").on("click", function () {
        var id = $(this).attr("href");
        scrollTo(id);
    });
  
    //for embedded links in the helpfile content we should scrollto as well
    $("#whcontent a").on("click", function () {
        var id = $(this).attr("href");
        if (id.indexOf("#") > -1) {//only scrollto if link is local to the page
            scrollTo(id);
        }
    });

    //set the left tree's position is fixed
    function setTreeHeight() {
        var viewHeight = document.body.clientHeight;
        var headerHeight = $("#whheader").height();
        $("#whsizer").css({ "position": "fixed"});
        $("#whsizer,#resizerCol").css({ "height": (viewHeight - headerHeight - 5), "min-height": "0" });
    }
    setTreeHeight();

    $(window).resize(function () {
        setTreeHeight();
    });

    function setScrollspy() {
        if($("#whtoc>ul").hasClass("nav")){
            $('body').scrollspy({
                offset: 160,
                target: '#whtoc'
            });
        }
    }
    setScrollspy();

    // if we scroll manually, it should open the tree and highlight the correct section 
    var choke = null;
    $('body').on('activate.bs.scrollspy', function (e) {
        //IE issue fixed:if the previous event is the tree expand event, the activate.bs.scrollspy leads to we cannot click the sibling tree node, so skip it
        if (e.isTrigger == 3 && e.namespace == "bs.scrollspy") {
            $("#whtoc li.active").parents(".parent_li").each(function () {
                var children = $(this).find(' > ul > li');
                children.show();
                $(this).find(">span").attr('title', 'Collapse this branch').addClass('ygtvlm').removeClass('ygtvtp');

                //close all siblings nodes
                $(this).siblings().find(">span").attr('title', 'Expand this branch').addClass('ygtvtp').removeClass('ygtvlm');
                $(this).siblings().find(' > ul > li').hide();
            });
            clearTimeout(choke);
            choke = setTimeout(function() {
              setScrollBar();
              treeScrollTop();
            }, 100);
        }
        
    });

    function treeScrollTop() {
        //$("#whsizer").css("overflow-y", "scroll");
        if ($("#whsizer").css("overflow-y").toLowerCase() == "scroll") {
            var nodeCount = $("#whtoc li:visible").size();
            var activeNodeIndex = 0;
            var visibleNode = $("#whtoc li:visible");
            for (var i = 0; i < visibleNode.size(); i++) {
                if ($(visibleNode[i]).is(".active")) {
                    activeNodeIndex = i + 1;
                }
            }
            var treeHeight = $("#whtoc>ul").height();
            $("#whsizer").scrollTop(treeHeight * (activeNodeIndex / nodeCount) - 200);
        }
    }

    setScrollBar();
});

