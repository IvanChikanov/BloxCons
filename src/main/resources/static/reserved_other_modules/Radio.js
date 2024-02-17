class Radio
{
    elements;
    onned;
    on;
    off;
    constructor(elementsArr, styleFunction, callBack)
    {
        this.elements = elementsArr;
        this.on = styleFunction.on;
        this.off = styleFunction.off;

        for(let oneElement of this.elements)
        {
            oneElement.addEventListener("click", 
            (e) => {
                if(this.onned)
                {
                    this.off(this.onned);         
                }
                this.on(e.target); 
                this.onned = e.target;
            });
            oneElement.addEventListener("click", callBack);
        }
    }
}
export {Radio};