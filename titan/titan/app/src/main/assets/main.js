
/*添加外层标签*/
function outDiv(inLabel, id)
{
    var outdiv = document.createElement("div");
    outdiv.id = id;
    outdiv.style.textAlign = "center";
    outdiv.appendChild(inLabel);
    document.getElementById("main").appendChild(outdiv);
}

/*添加图片*/
function addPhoto(path)
{
    var addImg = document.createElement("img");
    outDiv(addImg, path);
    addImg.outerHTML = "<img src='" + path + "' alt='图片存在问题' />";
}


/*确定图片是否存在于html页面中*/
function isExistedPhoto(id)
{
    if(document.getElementById(id))
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

/*返回整体网页*/
function wholeHtml()
{

    return unescape(document.getElementsByTagName('html')[0].outerHTML.toString());
}

