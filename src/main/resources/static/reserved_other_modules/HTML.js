class HTML
{
    gridArea;
    insideModule;
    static colorMap = new Map([["system", "#c9c9c9"], ["green", "#46B755"], ["blue","#3F8CFF"],["orange","#FF9432"],["old","#5AB52F"]]);
    static createAndAppend(type, parent = document.body)
    {
        let element = document.createElement(type);
        parent.appendChild(element);
        return element;
    }
    static create(type)
    {
       return document.createElement(type);
    }
    static addStyles(styleArray, element)
    {
        for(let e of element)
        {
            styleArray.forEach(st =>{e.classList.add(st)});
        }
    }
    static getValueAction(fromElement, storage, index)
    {
        storage[index] = fromElement.value;
    }
    static createPopup(child, closeAction = null)
    {
        let back = this.createAndAppend("DIV");
        this.addStyles(["fixed", "opacityBlack", "fullScreenSize", "grid"], [back]);
        let popup = this.createAndAppend("DIV", back);
        this.addStyles(["justifySelfCenter", "alignSelfCenter", "maxWidth90", "maxHeight90", "bRadius04rem" ,  "boxShadowCenter", "grid", "gapFivePix", "popupOpacity", "zIndex500"], [popup]);
        popup.style.padding = "10px";
        popup.style.gridTemplateRows = "repeat(2, auto)";
        let closeBut = this.createAndAppend("DIV", popup);
        this.addStyles(["justifySelfEnd", "alignSelfCenter", "pointer"], [closeBut]);
        closeBut.innerHTML = `<svg width="20" height="20" viewBox="0 0 18 17" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M1.04004 16L16.04 1" stroke="${getComputedStyle(document.documentElement).getPropertyValue('--mainColor')}" stroke-width="2" stroke-linecap="round"/><path d="M1.04004 1L16.04 16" stroke="${getComputedStyle(document.documentElement).getPropertyValue('--mainColor')}" stroke-width="2" stroke-linecap="round"/></svg>`;
        if(child)
        {
            child.style.gridArea = "";
            popup.appendChild(child);
        }
        closeBut.onclick = ()=>{if(closeAction)closeAction(); back.parentNode.removeChild(back)};
        return popup;
    }
    static async getJSON(pathPeacesArr)
    {
        let req = await fetch(HTML.editPath(pathPeacesArr));
        return await req.json();
    }
    static async sendJSON(pathPeacesArr, sendObject)
    {
        let req = await fetch(HTML.editPath(pathPeacesArr), {
            method: "POST",
            headers : {"Content-type":"application/json"},
            body: JSON.stringify(sendObject) 
        });
        try
        {
            return await req.json();
        }
        catch(ex)
        {
            if(ex.name != "SyntaxError")
            {
                console.log(ex.message);
            }
        }
    }
    static async deleteRequest(pathPeacesArr)
    {
       await fetch(HTML.editPath(pathPeacesArr));
    }
    static editPath(pathPeacesArr)
    {
        let slash = "/"
        let fullPath = "";
        for(let p of pathPeacesArr)
        {
            fullPath += slash + p;
        }
        return fullPath;
    }
    static async getEmptyUnit(cell_id)
    {
        return await HTML.getJSON(["pages","get_empty_unit", cell_id]);
    }
    static createFileloaderElement(extensions, callBack)
    {
        let fileloader = HTML.create("INPUT");
        fileloader.type = "file";
        fileloader.setAttribute("accept", extensions);
        HTML.addStyles(["fullScreenSize", "borderBox"],[fileloader]);
        fileloader.addEventListener("change", ()=>{
            callBack(fileloader.files[0]);
        });
        fileloader.addEventListener("dragover", (e)=>{
            e.preventDefault();
        });
        fileloader.addEventListener("drop", (e)=>{
            e.preventDefault();
            callBack(e.dataTransfer.files[0]);
        });
        return fileloader;
    }
    static saveUnit(unit)
    {
        fetch("/pages/save_unit", {
            method: "POST",
            headers: {"Content-type": "application/json"},
            body: JSON.stringify(unit)
        }).then(res => res.text()).catch(err => console.log(err));
    }
    static async updateImageUnit(file, unit)
    { 
        let saveIm = unit.imageId? HTML.updateImage : HTML.createImage;
        unit.imageId = await saveIm(file, unit.imageId);
        this.saveUnit(unit);
    }
    static async createImage(file, id)
    {
        let fd = new FormData();
        fd.append("image", file);
        let request = await fetch("/create_image", {
            method: "POST",
            body: fd});
        return await request.text();

    }
    static async updateImage(file, id)
    {
        let fd = new FormData();
        fd.append("image", file);
        fd.append("id", id);
        let request = await fetch("/update_image", {
            method: "POST",
            body: fd});
        return await request.text();
    }
    static async showImage(id)
    {
        let request = await fetch(id);
        let response = await request.json();
        return `data:${response.contentType};${response.data}`;
    }
}
export {HTML}