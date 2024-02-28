class ToolsKeeper
{
    toolsUnits = [];
    radioArray = [];
    constructor(innerAndCallbackArray)
    {
        for(let u of innerAndCallbackArray)
        {
            this.toolsUnits.push(this.createButton(u));
        }
        if(this.radioArray.length > 0)
        {
            this.radioUnits();
        }
    }
    createButton(toolUnit)
    {
        let button = HTML.create("DIV");
        button.innerHTML = "src" in toolUnit? `<img src="${toolUnit.src}" style="max-width:80%; max-height:80%;">`: toolUnit.title[0].toUpperCase();
        button.classList.add("oneGridTool");
        button.style = "display: flex; align-items:center; justify-content:center;";
        button.onclick = toolUnit.call;
        button.title = toolUnit.title;
        if(toolUnit.radio)
        {
            this.radioArray.push(button);
        }
        return button;
    }
    radioUnits()
    {
        new Radio(this.radioArray, 
            {
             on: function(element){element.classList.replace("oneGridTool", "oneGridToolActive")},
             off: function(element){element.classList.replace("oneGridToolActive", "oneGridTool")}
            }, null);
        this.radioArray[0].dispatchEvent(new Event("click"));
    }
    setDefault()
    {
        this.radioArray[0].dispatchEvent(new Event("click"));
    }
}
export {ToolsKeeper};