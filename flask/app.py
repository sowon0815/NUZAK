import openai
openai.api_key = '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'

from flask import Flask
app = Flask(__name__)

@app.route('/')
def index():
   return "hi"

@app.route('/1',defaults={'data':'index'})
@app.route('/1/<data>')
def boards1(data):
  response1 = openai.Completion.create(
    model='curie:ft-ewha-womans-university-2021-11-04-06-46-07',
    prompt=data+'\n\n###\n\n',
    max_tokens=1200,
    temperature=0.8,
    n=3,
    stop=[" END"],
    frequency_penalty = 0.2
    )
  taletext = "Text:" + response1.choices[1].text + " \n\nKeywords:"
  response2 = openai.Completion.create(
    model='curie:ft-ewha-womans-university-2021-11-04-06-21-39',
    prompt=taletext,
    temperature=0.3,
    max_tokens=60,
    top_p=1.0,
    frequency_penalty=0.78,
    presence_penalty=0.0,
    stop=["\n"]
  )

  return response1.choices[1].text + "*****" + response2.choices[0].text

@app.route('/2',defaults={'data':'index'})
@app.route('/2/<data>')
def boards2(data):
  response1 = openai.Completion.create(
    model='curie:ft-ewha-womans-university-2021-11-04-07-39-07',
    prompt=data+'\n\n###\n\n',
    max_tokens=500,
    temperature=0.8,
    n=3,
    stop=[" END"],
    frequency_penalty = 0.2
    )

  taletext = "Text:" + response1.choices[1].text + " \n\nKeywords:"
  response2 = openai.Completion.create(
    model='curie:ft-ewha-womans-university-2021-11-04-06-21-39',
    prompt=taletext,
    temperature=0.3,
    max_tokens=60,
    top_p=1.0,
    frequency_penalty=0.8,
    presence_penalty=0.0,
    stop=["\n"]
  )

  return response1.choices[1].text + "*****" + response2.choices[0].text

@app.route('/3',defaults={'data':'index'})
@app.route('/3/<data>')
def boards3(data):
  response1 = openai.Completion.create(
    model='curie:ft-ewha-womans-university-2021-11-04-08-06-13',
    prompt=data+'\n\n###\n\n',
    max_tokens=500,
    temperature=0.8,
    n=3,
    stop=[" END"],
    frequency_penalty = 0.2
    )
  taletext = "Text:" + response1.choices[1].text + " \n\nKeywords:"
  response2 = openai.Completion.create(
    model='curie:ft-ewha-womans-university-2021-11-04-06-21-39',
    prompt=taletext,
    temperature=0.3,
    max_tokens=60,
    top_p=1.0,
    frequency_penalty=0.8,
    presence_penalty=0.0,
    stop=["\n"]
  )

  return response1.choices[1].text + "*****" + response2.choices[0].text

if __name__ == '__main__':  
   app.run('0.0.0.0', port=8080, debug=True)