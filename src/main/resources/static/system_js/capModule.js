import { HTMLFather as HTML } from "../system_js/HTMLFather.js";
class CapModule
{
    cellId;
    textDiv;
    unit;
    svgRerender = {};
    init(cellId)
    {
        this.cellId = cellId;
        return this.createElement();
    }
    createElement()
    {
        let element = HTML.create("DIV");
        HTML.addStyles(["cap", "boxShadowCenter", "bottomMargin10px"], [element]);
        element.style.minHeight = "100px";
        let finger = document.createElementNS("http://www.w3.org/2000/svg", "svg");
        finger.innerHTML = `<path d="M0,0H65a0,0,0,0,1,0,0V19.7A45.3,45.3,0,0,1,19.7,65H0a0,0,0,0,1,0,0V0A0,0,0,0,1,0,0Z" id="fingerBack" style="fill:${HTML.colorMap.get(document.documentElement.getAttribute("theme"))}"/><path d="M22.3,33.2V21.1a2.9,2.9,0,0,1,2.3-2.9A3.1,3.1,0,0,1,28,19.7a4.1,4.1,0,0,1,.3,1.6c.1,1.7,0,3.4,0,5.2a3.3,3.3,0,0,1,2.9-.2A3.1,3.1,0,0,1,33,28.5a3.1,3.1,0,0,1,2.8-.2,3.3,3.3,0,0,1,1.8,2.3,2.9,2.9,0,0,1,2.2-.4A2.9,2.9,0,0,1,42.3,33V44.2A12.4,12.4,0,0,1,31.9,55.9,12,12,0,0,1,18.3,46c-.8-4.2-1.5-8.4-2.2-12.6a3,3,0,0,1,2.3-3.6A3,3,0,0,1,22,32.3a3,3,0,0,1,.2,1ZM23.7,31h0v9.9a.7.7,0,0,1-.5.8.7.7,0,0,1-.9-.6c-.3-1.4-.5-2.7-.8-4.1s-.5-3-.9-4.5a1.6,1.6,0,0,0-2.1-1.3,1.8,1.8,0,0,0-1,2.2l2.1,12.1a10.5,10.5,0,0,0,12.3,9,10.9,10.9,0,0,0,9-10.1c.1-3.8,0-7.5,0-11.3a1.4,1.4,0,0,0-.6-1.2,1.5,1.5,0,0,0-1.7-.2,1.6,1.6,0,0,0-1,1.5v4.4c0,.6-.4,1-.8.8s-.5-.5-.5-.9V31.1a1.7,1.7,0,0,0-3.3.1v4.3a.8.8,0,0,1-.4.7c-.5.3-1-.1-1-.8v-6a3.4,3.4,0,0,0-.1-1,1.6,1.6,0,0,0-1.8-.9,1.5,1.5,0,0,0-1.3,1.6v3.7a25.3,25.3,0,0,1-.1,2.8c0,.5-.2.7-.7.7a.7.7,0,0,1-.6-.7V21.1a1.7,1.7,0,0,0-1.2-1.6,1.7,1.7,0,0,0-2.1,1.7Z" style="fill:#fff"/><path d="M35.9,17.7a11,11,0,0,1-1.6,5.6c-.3.6-.7.7-1.2.4s-.5-.6-.1-1.2a9.4,9.4,0,0,0-.6-10.4,9.3,9.3,0,0,0-16.6,4.4,9,9,0,0,0,3.9,8.9l.4.2a.7.7,0,0,1,.2,1.1.7.7,0,0,1-1.1.2,10.5,10.5,0,0,1-4.3-5A10.4,10.4,0,0,1,17,10.4C20,7,23.9,6,28.3,7.3a10.5,10.5,0,0,1,7.5,9C35.9,16.8,35.8,17.3,35.9,17.7Z" style="fill:#fff"/><path d="M31.7,17.6a6.3,6.3,0,0,1-.9,3.3c-.3.5-.7.7-1.1.5s-.4-.6-.1-1.2a5.3,5.3,0,1,0-9,.5c.1.1.2.2.2.3s.3.8,0,1a.7.7,0,0,1-1.1-.2,16.7,16.7,0,0,1-1.2-2.3,6.7,6.7,0,1,1,13.1-2.9A3.4,3.4,0,0,1,31.7,17.6Z" style="fill:#fff"/>`;
        finger.style.float = "left";
        finger.setAttribute("viewBox", "0 0 65 65");
        finger.setAttribute("width", "50px");
        finger.setAttribute("height", "50px");
        element.appendChild(finger);
        this.svgRerender = {
            id: "fingerBack",
            attr: { 
                name: "style",
                inside: "fill:"
            }
        };
        this.textDiv = HTML.createAndAppend("DIV", element);
        this.textDiv.style.marginLeft = "50px";
        this.textDiv.style.width = "calc(100% - 50px)";
        this.textDiv.style.height = "100%";
        return element;
    }
    updateVisual()
    {
        this.textDiv.innerHTML = this.unit[0].textContent;
    }
}
//class separator

class CapModuleChild
{
    async init(cellId)
    {
        window.addEventListener("changeTheme", (e) =>{ 
            document.getElementById(this.svgRerender.id).setAttribute(this.svgRerender.attr.name, this.svgRerender.attr.inside + e.detail.main)});
        this.cellId = cellId;
        await this.loadUnits();   
        let elem = super.createElement();       
        HTML.addStyles(["hover"],[this.textDiv]);
        this.updateVisual();
        this.textDiv.onclick = async ()=>
        {
            let editson;
            await import("/static/ck_box/build/ckeditor.js");
            let insidePopup = HTML.create("DIV");
            HTML.createPopup(insidePopup, async ()=>{
                this.textDiv.innerHTML = editson.getData(); 
                editson.destroy(); 
                this.unit[0].textContent = this.textDiv.innerHTML;
                await HTML.sendJSON(["pages", "save_unit"], this.unit[0]);});
            ClassicEditor.create(insidePopup).then( editor => {editson = editor; editor.setData(this.textDiv.innerHTML)}).catch( error => {console.error( error );} );
        }    
        return elem;
    }
    async loadUnits()
    {
        this.unit = await HTML.getJSON(["pages", "get_all_units", this.cellId]);
        if(this.unit.length < 1)
        {
            this.unit.push(await HTML.getEmptyUnit(this.cellId));
        }
    }
}
export{CapModule, CapModuleChild}