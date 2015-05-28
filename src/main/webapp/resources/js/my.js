/*
 $(document).ready(function () {
 console.log('aaaaaaaaaaaaaaaaaa');
 $('.ui-panel-title').one("inview", function () {
 //div:sampleの直前要素のtextを取得
 console.log($(this).prev().text());
 });
 
 $('.sample').on('inview', function(event, isInView, visiblePartX, visiblePartY) {
 if (isInView) {
 //要素が見えたときに実行する処理
 console.log('aaa');
 } else {
 //要素が見えなくなったときにunbindする
 $(this).unbind('inview');
 }
 });
 
 });
 */
//コンテンツが追加されたことを検出し、イベントを追加していく
$(document).ready(function() {
    $('#main').on('DOMSubtreeModified propertychange', function() {
        test()
    });
});

function ajaxStatusHandler(xhr, status, args) {
    console.log('ajaxStatusHandler');
}
/*
 $(document).on('inview', '.ui-panel-title', function () {
 console.log($(this).text());
 $(this).off('inview');
 });
 */
function test() {

    var text = $('.ui-panel-title').text();
    console.log("fuc test"+text);
    $('.ui-panel-title').one("inview", function () {
        //div:sampleの直前要素のtextを取得
        console.log($(this).text());
    });
}