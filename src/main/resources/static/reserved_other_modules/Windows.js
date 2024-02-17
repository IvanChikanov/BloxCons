class Windows
{
    wnd;
    back;
    #insideBlock;
    closeEvent;
    #type;
    #stylesArray = ["bRadius04rem" , "boxShadowCenter", "grid", "gapFivePix", "popupOpacity", "zIndex500", "border1pix"];
    #isClosed = true;
    #showedElement;
    constructor(type)
    {
        this.#type = type;
        switch(type)
        {
            case "unclosable-full":
                this.#isClosed = false;
            case "content-full":
                this.#stylesArray.push("justifySelfCenter");
                this.#stylesArray.push("alignSelfCenter");
                this.#createBack();
                break;
            case "freePosition":
                this.#stylesArray.push("absolute");
                break;
            case "unclosable-auto":
                this.#isClosed = false;
            case "content-auto":
                this.#stylesArray.push("justifySelfCenter");
                this.#stylesArray.push("alignSelfCenter");
                this.#createBack();
                break;
        }
        this.#createWindow();
        this.prepareToShow();
    }
    #createBack()
    {
        this.back = HTML.createAndAppend("DIV");
        HTML.addStyles(["fixed", "opacityBlack", "fullScreenSize", "grid"], [this.back]);
    }
    #createWindow()
    {
        this.wnd = HTML.create("DIV");
        HTML.addStyles(this.#stylesArray, [this.wnd]);
        this.wnd.style.padding = "10px";
        this.wnd.style.gridTemplateRows = "25px auto";
        if(this.#isClosed)
        {
            let closeBut = HTML.createAndAppend("DIV", this.wnd);
            HTML.addStyles(["justifySelfEnd", "alignSelfCenter", "pointer"], [closeBut]);
            closeBut.innerHTML = `<svg width="20" height="20" viewBox="0 0 18 17" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M1.04004 16L16.04 1" stroke="${getComputedStyle(document.documentElement).getPropertyValue('--mainColor')}" stroke-width="2" stroke-linecap="round"/><path d="M1.04004 1L16.04 16" stroke="${getComputedStyle(document.documentElement).getPropertyValue('--mainColor')}" stroke-width="2" stroke-linecap="round"/></svg>`;
            closeBut.onclick = this.closeWidthAction.bind(this);
        }
    }
    prepareToShow()
    {
        if(this.#type != "freePosition")
        {
            this.back.appendChild(this.wnd);
            this.#showedElement = this.back;
        }
        else
        {
            this.#showedElement = this.wnd;
        }
    }
    setInside(htmlElement)
    {
        this.#insideBlock = htmlElement;
        if(this.#type.includes("-full"))
        {
            HTML.addStyles(["fullWindow","scrollY"],[this.#insideBlock]);
            HTML.addStyles(["fullWindow"],[this.wnd]);
        }
        else if(this.#type.includes("-auto"))
        {
            HTML.addStyles(["fullWindow", "scrollY"],[this.#insideBlock]);
            HTML.addStyles(["autoWindow"],[this.wnd]);
        }

    }
    show(positionElement = null)
    {
        if(positionElement)
        {
            let rect = positionElement.getBoundingClientRect();
            this.#showedElement.style.top = rect.top + window.scrollY + "px";
            this.#showedElement.style.left = rect.left + window.scrollX + "px";
            this.#showedElement.style.transform = "translate(-50%, -50%)";
        }
        this.wnd.appendChild(this.#insideBlock);
        document.body.appendChild(this.#showedElement);
    }
    closeAction(eventFunction)
    {
        this.closeEvent = eventFunction;
    }
    closeWidthAction()
    {
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
    }
    close()
    {
        document.body.removeChild(this.#showedElement);
    }
}
export {Windows};