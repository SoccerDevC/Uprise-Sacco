<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Deposits extends Model
{
    protected $table = 'deposits'; 
    public $timestamps = false;

    protected $fillable = ['member_id', 'receipt_number', 'amount', 'date'];

    protected $primaryKey = 'receipt_number';
    public $incrementing = false;

    public function setDateAttribute($value)
    {
        // Convert the date to 'yyyy-mm-dd' format
        $this->attributes['date'] = date('Y-m-d', strtotime($value));
    }
}

