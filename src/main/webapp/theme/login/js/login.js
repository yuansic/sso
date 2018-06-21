$(document).ready(function(){
    var hh  = $(window).height();
    $(".bgs").height(hh);

    // 登陆与注册模块的切换
    $(".btn-right").click(function(){
         $(".login").animate({ marginLeft  : '-450px'},200);
         $(".logsss").animate({ left  : '-0px'},200);
         return false;
    })
    $(".btn-left").click(function(){
        $(".login").animate({ marginLeft  : '0px'},200);
        $(".logsss").animate({ left  : '450px'},200);
        return false;
   })
})
