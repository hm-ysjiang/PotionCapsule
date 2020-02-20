#!/bin/bash
# Generates lang files and copies the existing translations to the new file with the same format
# Translators should run this, enter the correct lang code, and saves the result using UTF-8 without BOM
# Written by hmysjiang

_PATH='src/main/resources/assets/potioncapsule/lang'

read -p 'Enter the desired language name: ' _LANG
if [[ $_LANG ]] && [[ $_LANG != 'en_us' ]] ; then
	echo "Generating lang file for $_LANG..."
	if [[ -s "$_PATH/$_LANG.json" ]] ; then
		echo -n "Found existing lang file, copying existing translations..."
		cp "$_PATH/$_LANG.json" "$_PATH/tmp_$_LANG.json"
		rm "$_PATH/$_LANG.json"
		cp "$_PATH/en_us.json" "$_PATH/$_LANG.json"
		
		_DOTS=0
		while read _LINE; do
			_KEY=`echo $_LINE | awk -F': ' '{ print $1 }'`
			if [[ $_KEY ]] && [[ $_KEY != '{' ]] && [[ $_KEY != ',' ]] && [[ ${_KEY:1:1} != '_' ]] ; then
				_OLD=`grep -F $_KEY "$_PATH/tmp_$_LANG.json"`
				if [[ $_OLD ]] ; then
					_PATT_LINE="$(<<< "$_LINE" sed -e 's`[][\\/.*^$]`\\&`g')"
					_PATT_OLD="$(<<< "$_OLD" sed -e 's`[][\\/.*^$]`\\&`g')"
					_PATT_OLD=${_PATT_OLD:1:${#_PATT_OLD}-1}
					sed -i "s/$_PATT_LINE/$_PATT_OLD/" "$_PATH/$_LANG.json"
				fi
			fi
			
			if [[ _DOTS -eq 3 ]] ; then
				_DOTS=0
				for ((i=1; i<=3; i++))
				do
					echo -ne "\b"
				done
				for ((i=1; i<=3; i++))
				do
					echo -n ' '
				done
			elif [[ _DOTS -eq 0 ]] ; then
				echo -ne "\b\b\b."
				((_DOTS+=1))
			else
				echo -n .
				((_DOTS+=1))
			fi
		done < "$_PATH/en_us.json"
		
		for ((i=1; i<=$_DOTS; i++))
		do
			echo -ne "\b"
		done
		for ((i=1; i<=$_DOTS; i++))
		do
			echo -n ' '
		done
		echo
		echo Finished copying
		
		rm "$_PATH/tmp_$_LANG.json"
	else
		cp "$_PATH/en_us.json" "$_PATH/$_LANG.json"
		echo "Generated $_LANG.json"
	fi
elif [[ $_LANG ]] ; then
	echo 'Why are you trying to translate en_us to en_us?'
else
	echo 'The input is null, exitting...'
fi

# Fake pause
echo
read -n 1 -p "Press any key to continue..." _PAUSE
if [[ $_PAUSE ]] ; then
	echo -ne '\b \n'
fi
