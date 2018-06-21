/**
 * Created by sh-yangxiao on 2018/2/5.
 */
//左侧父菜单点击
$(".pMenuElement").on("click",function(){
    var parentMenu = $(this).parent(".pMenuLi").siblings();//pMenuLi的兄弟节点
    var chileMenu = $(this).parent(".pMenuLi").children(".childMenu");
    if(chileMenu.hasClass("showChildMenu")){
        chileMenu.removeClass("showChildMenu").addClass("hideChildMenu");
        $(this).children(".pMenuHover").removeClass("menuArrowTp").addClass("menuArrowRt");
    }else{
        var pChildMenu ;
        parentMenu.each(function(){ //遍历兄弟节点
            pChildMenu = $(this).children(".childMenu");
            if(pChildMenu.hasClass("showChildMenu")){
                pChildMenu.removeClass("showChildMenu").addClass("hideChildMenu");
                $(this).children(".pMenuElement").children(".pMenuHover").removeClass("menuArrowTp").addClass("menuArrowRt");
            }
        });
        chileMenu.removeClass("hideChildMenu").addClass("showChildMenu");
        $(this).children(".pMenuHover").removeClass("menuArrowRt").addClass("menuArrowTp");
    }
});
//左侧子菜单跳转
$(".childMenu li").on("click",function(){
    var currentPath = window.location.href.split("?")[0];
    //alert(currentPath);
    //var  currentPath.split("\\");
    var fileUrl = $(this).parent(".childMenu").attr("data-file");
    var pathUrl = fileUrl + $(this).attr("data-url");
   // if(!$(this).hasClass("childMenuActive")){
        $(this).siblings().removeClass("childMenuActive");
        $(this).addClass("childMenuActive");
   // }

});
/*
var hh = window.screen.height;
$(".menu").height(hh);
*/

