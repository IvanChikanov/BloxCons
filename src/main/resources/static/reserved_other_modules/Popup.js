class Popup
{
    back;
    popup;
    insideGrid;
    rows;
    cols;
    closeEvent;
    rowPercentSize = [];
    colPercentSize = [];
    constructor(rows, cols)
    {
        this.#createPopupElement();
        this.rowPercentSize = Array(rows).fill("auto");
        this.colPercentSize = Array(cols).fill("auto");
        this.insideGrid = HTML.createAndAppend("DIV", this.popup);
        this.insideGrid.style.display = "grid";
        this.insideGrid.style.gridTemplateRows = this.rowPercentSize.join(" ");
        this.insideGrid.style.gridTemplateColumns = this.colPercentSize.join(" ");
        this.insideGrid.style.gap = "3px";
        //this.insideGrid.style.aspectRatio = "16/10";
    }
    addElement(x, y, element)
    {
        element.style.gridArea = `${x}/${y}/${x}/${y}`;
        this.insideGrid.appendChild(element);
    }
    show()
    {
        document.body.appendChild(this.back);
    }
    setCloseEvent(func)
    {
        this.closeEvent = func;
    }
    #createPopupElement()
    {
        this.back = HTML.createAndAppend("DIV");
        HTML.addStyles(["fixed", "opacityBlack", "fullScreenSize", "grid"], [this.back]);
        this.popup = HTML.createAndAppend("DIV", this.back);
        HTML.addStyles(["justifySelfCenter", "alignSelfCenter", "maxWidth90", "maxHeight90", "bRadius04rem" ,  "boxShadowCenter", "grid", "gapFivePix", "popupOpacity", "zIndex500", "border1pix"], [this.popup]);
        this.popup.style.padding = "10px";
        this.popup.style.gridTemplateRows = "repeat(2, auto)";
        let closeBut = HTML.createAndAppend("DIV", this.popup);
        HTML.addStyles(["justifySelfEnd", "alignSelfCenter", "pointer"], [closeBut]);
        closeBut.innerHTML = `<svg width="20" height="20" viewBox="0 0 18 17" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M1.04004 16L16.04 1" stroke="${getComputedStyle(document.documentElement).getPropertyValue('--mainColor')}" stroke-width="2" stroke-linecap="round"/><path d="M1.04004 1L16.04 16" stroke="${getComputedStyle(document.documentElement).getPropertyValue('--mainColor')}" stroke-width="2" stroke-linecap="round"/></svg>`;
        closeBut.onclick = ()=>{
            if(this.closeEvent)
            {
                if(this.closeEvent())
                {
                    this.close();
                }   
            }
            else
            {
                this.close();
            }
        };
    }
    setSize(percent)
    {
        this.insideGrid.style.width = `${percent}vw`;
        this.insideGrid.style.height = `${percent}vh`;
    }
    sizeShow()
    {
        this.popup.style.opacity = "0";
        document.body.appendChild(this.back);
        let rect = this.insideGrid.getBoundingClientRect();
        let area = rect.width * rect.height;
        this.insideGrid.style.width = Math.sqrt(area) + "px";
        this.insideGrid.style.height = this.insideGrid.style.width;
        this.popup.style.opacity = "";
    }
    rowSize(numberOfRow, percent)
    {
        this.rowPercentSize[numberOfRow - 1] = `${percent}%`;
        this.insideGrid.style.gridTemplateRows = this.rowPercentSize.join(" ");
    }
    colSize(numberOfCol, percent)
    {
        this.colPercentSize[numberOfCol - 1] = `${percent}%`;
        this.insideGrid.style.gridTemplateColumns = this.colPercentSize.join(" ");
    }
    close()
    {
        document.body.removeChild(this.back);
    }
}
export {Popup};